package com.ai.platform.agent.client.incoming.processor.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractCommandProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.exception.AgentServerException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的结束会话命令处理
 * 
 * @author Administrator
 *
 */
public class CloseSSHCommandMessageProcessor extends AbstractCommandProcessor {

	public static Logger logger = LogManager.getLogger(CloseSSHCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String sid = super.command.getSid();

		String key = sid;

		if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {
			throw new AgentServerException("当前会话容器未找到sid:{}的会话信息");
		}

		UserChannelInfo usci = ShellChannelCollectionUtil.userChannelMap.get(key);

		usci.checkChannelIsOpen();

		Session sshSession = usci.getSession();
		Channel sshChannel = usci.getChannel();

		if (sshChannel != null && !sshChannel.isClosed()) {
			logger.info("客户端关闭sid:{}的channel", sid);
			sshChannel.disconnect();
		}

		if (sshSession != null && sshSession.isConnected()) {
			logger.info("客户端关闭sid:{}的session", sid);
			sshSession.disconnect();
		}

		usci = null;

		ShellChannelCollectionUtil.userChannelMap.remove(key);

		logger.info("客户端会话去除sid:{}的会话信息，会话容器还剩{}", sid, ShellChannelCollectionUtil.userChannelMap.size());
	}
}
