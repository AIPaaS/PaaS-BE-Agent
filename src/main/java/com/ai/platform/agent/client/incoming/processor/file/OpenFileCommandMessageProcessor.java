package com.ai.platform.agent.client.incoming.processor.file;

import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserSftpChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractFileProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.entity.UserSessionInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.ChannelSftp;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的开启文件传输处理
 * 
 * @author Administrator
 *
 */
public class OpenFileCommandMessageProcessor extends AbstractFileProcessor {

	public static Logger logger = LogManager.getLogger(OpenFileCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		logger.info("服务端发来指令，内容：{}", super.command.getDataByteArray());
		String uid = super.command.getUid();
		String sid = super.command.getSid();
		String fid = super.command.getFid();

		String key = sid;
		String fkey = fid;
		try {

			if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {

				UserSftpChannelInfo usci = new UserSftpChannelInfo();
				UserSessionInfo usi = new UserSessionInfo();
				usi.setUid(uid);
				usi.setSid(sid);

				HostFileInfo sftpHostInfo = JSON.parseObject(message, HostFileInfo.class);

				usci.firstSSHAction((HostInfo) sftpHostInfo);
				usci.setFileInfo(sftpHostInfo);

				ChannelSftp sftp = (ChannelSftp) usci.getChannel();

				sftp.cd(sftpHostInfo.getPath());
				OutputStream outstream = sftp.put(sftpHostInfo.getFileName());
				
				usci.setOutstream(outstream);

				ShellChannelCollectionUtil.userChannelMap.put(key, usci);
			} else {
				logger.info("当前 key:{}已经登录", sid);
			}
		} catch (Exception e) {
			logger.error("创建文件失败{}", e);
			throw new AgentServerException("创建文件失败");
		}

	}
}
