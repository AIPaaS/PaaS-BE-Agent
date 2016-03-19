package com.ai.platform.agent.web.services;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.ai.platform.agent.entity.AgentSftpInfo;
import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.entity.Server2ClientFileMsgMVO;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class ShellFileCommandSer {

	public static Logger logger = LogManager.getLogger(ShellFileCommandSer.class);

	/***
	 * 获取sftp通道
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	private AuthChannelInfo getAuthChannelInfo(Server2ClientFileMsgMVO sendMsg) throws Exception {

		String bussMsg = sendMsg.getBussInfo();
		AgentSftpInfo commandInfo = sendMsg.getTransFileInfo();

		String aid = commandInfo.getAid();
		String uid = commandInfo.getUid();
		String sid = commandInfo.getSid();
		String key = aid;

		if (Strings.isBlank(aid)) {
			throw new Exception("请指定Agent标识");
		}

		if (Strings.isBlank(uid)) {
			throw new Exception("请指定uid标识");
		}

		if (Strings.isBlank(sid)) {
			throw new Exception("请指定sid标识");
		}

		if (!ChannelCollectionUtil.ctxMap.containsKey(key)) {
			throw new Exception("指定的客户端未连接");
		}

		AuthChannelInfo channel = ChannelCollectionUtil.ctxMap.get(key);
		if (!channel.getCtx().channel().isActive()) {
			throw new Exception("指定的客户端未激活");
		}
		return channel;
	}

	/***
	 * 传输文件
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(Server2ClientFileMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);

		JSch jsch = new JSch();
		long startTime = System.currentTimeMillis();
		AgentSftpInfo transFileInfo = sendMsg.getTransFileInfo();
		HostFileInfo srcFileSftpInfo = transFileInfo.getSrcFileSftpInfo();
		HostFileInfo descFileInfo = transFileInfo.getDescFileSftpInfo();

		String host = srcFileSftpInfo.getHost();
		String user = srcFileSftpInfo.getUserName();
		final Session session = jsch.getSession(user, host, 22);

		String passwd = srcFileSftpInfo.getPassword();
		session.setPassword(passwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();

		Channel ftpChannel = session.openChannel("sftp");
		ftpChannel.connect();
		ChannelSftp sftp = (ChannelSftp) ftpChannel;

		sftp.cd(srcFileSftpInfo.getPath());
		InputStream instream = sftp.get(srcFileSftpInfo.getFileName());

		initAgentSftpChannel(sendMsg);
		try {
			TransFileBySftpChannel(sendMsg, instream);
		} catch (Exception e) {
			throw e;
		} finally {
			destroyAgentSftpChannel(sendMsg);
		}

		ResultMessageMVO result = new ResultMessageMVO(ResultCodeConstants.OK, ResultCodeConstants.OK_MSG);
		return JSON.toJSONString(result);
	}

	/***
	 * 初使化sftp通道
	 * @param sendMsg
	 * @throws Exception
	 */
	private void initAgentSftpChannel(Server2ClientFileMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);

		String fileContent = JSONObject.toJSONString(sendMsg.getTransFileInfo().getDescFileSftpInfo());
		logger.info("服务端ssh到客户端，信息为：{}", fileContent);

		byte[] openFileCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_FILE_OPEN,
				sendMsg.getTransFileInfo().getUid().getBytes(), sendMsg.getTransFileInfo().getSid().getBytes(),
				sendMsg.getTransFileInfo().getFid().getBytes(), fileContent.getBytes());
		channel.getCtx().channel().writeAndFlush(openFileCommandArray);
	}

	/***
	 * 传输文件
	 * @param sendMsg
	 * @param instream
	 * @throws Exception
	 */
	private void TransFileBySftpChannel(Server2ClientFileMsgMVO sendMsg, InputStream instream) throws Exception {
		byte b[] = new byte[1024 * 100];
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);
		// 发送传输文件命令
		byte[] fileContentArray;
		int i = 0;
		int n;
		int m = 0;
		while ((n = instream.read(b)) != -1) {
			byte[] tmpBytes = new byte[n];
			System.out.println("---" + n);
			System.arraycopy(b, 0, tmpBytes, 0, n);

			for (int t = 0; t < 19 && (m = instream.read(b)) != -1; t++) {
				Thread.sleep(100);
				byte[] tmpBytes1 = new byte[m];
				System.arraycopy(b, 0, tmpBytes1, 0, m);
				tmpBytes = ByteArrayUtil.mergeByteArray(tmpBytes, tmpBytes1);
			}

			fileContentArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_FILE_WRITE,
					sendMsg.getTransFileInfo().getUid().getBytes(), sendMsg.getTransFileInfo().getSid().getBytes(),
					sendMsg.getTransFileInfo().getFid().getBytes(), tmpBytes);
			channel.getCtx().channel().writeAndFlush(fileContentArray);
			System.out.println("======" + tmpBytes.length + "   " + i++);
		}

		instream.close();
	}

	private void destroyAgentSftpChannel(Server2ClientFileMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);

		String fileContent = JSONObject.toJSONString(sendMsg);
		logger.info("服务端ssh到客户端，信息为：{}", fileContent);

		byte[] closeFileCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_FILE_CLOSE,
				sendMsg.getTransFileInfo().getUid().getBytes(), sendMsg.getTransFileInfo().getSid().getBytes(),
				sendMsg.getTransFileInfo().getFid().getBytes(), fileContent.getBytes());
		channel.getCtx().channel().writeAndFlush(closeFileCommandArray);
	}

}
