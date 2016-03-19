package com.ai.platform.agent.client.incoming.processor.file;

import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserSftpChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractFileProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.exception.AgentServerException;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的命令处理
 * 
 * @author Administrator
 *
 */
public class TransFileMessageProcessor extends AbstractFileProcessor {

	public static Logger logger = LogManager.getLogger(TransFileMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String uid = super.command.getUid();
		String sid = super.command.getSid();
		String fid = super.command.getFid();
		String command = super.command.getMessage();
		logger.info("服务端发来指令，内容大小：{}", super.command.getDataByteArray().length);

		String key = sid;
		String skey = fid;
		if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {
			throw new AgentServerException("当前会话容器未找到sid:{}的会话信息");
		}
		try {
			UserSftpChannelInfo usci = (UserSftpChannelInfo) ShellChannelCollectionUtil.userChannelMap.get(key);
			usci.checkChannelIsOpen();
			OutputStream outstream = usci.getOutstream();

			HostFileInfo sftpHostInfo = usci.getFileInfo();

			outstream.write(super.command.getDataByteArray());
			logger.info("文件{}/{}已经写入{}字节", sftpHostInfo.getPath(), sftpHostInfo.getFileName(),
					sftpHostInfo.addFileLength(super.command.getDataByteArray().length));
			outstream.flush();

		} catch (Exception e) {
			logger.error("写文件内容失败", e);
			throw new AgentServerException("写文件内容失败");
		}
	}
}
