package com.ai.platform.agent.client.incoming.handler;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.processor.NoCommandProcessor;
import com.ai.platform.agent.client.incoming.processor.command.CloseSSHCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.command.ExecCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.command.OpenSSHCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.command.SimpleCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.file.CloseFileCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.file.OpenFileCommandMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.file.SimpleFileMessageProcessor;
import com.ai.platform.agent.client.incoming.processor.file.TransFileMessageProcessor;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.AgentServerCommandConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IncomingMessageHandler extends SimpleChannelInboundHandler<byte[]> {

	public static Logger logger = LogManager.getLogger(IncomingMessageHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("接收信息内容：[{}]", msg);
		}
		if (msg.length <= AgentConstant.PACKAGE_TYPE_LENGTH) {
			throw new MessageParserException("msg[] is too short.");
		}
		byte[] type = new byte[AgentConstant.PACKAGE_TYPE_LENGTH];
		byte[] data = new byte[msg.length - 2];
		System.arraycopy(msg, 0, type, 0, AgentConstant.PACKAGE_TYPE_LENGTH);
		System.arraycopy(msg, 2, data, 0, data.length);

		IMessageProcessor messageProcessor = getProcessor(type);

		messageProcessor.init(data);
		messageProcessor.proc(ctx);
	}

	private IMessageProcessor getProcessor(byte[] type) throws AgentServerException {
		logger.info("接收信息类型：[{}]", type);
		IMessageProcessor messageProcessor = null;
		if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND)) {
			// 一次性脚本命令处理
			messageProcessor = new SimpleCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SIMP_FILE_COMMAND)) {
			// 文件处理
			messageProcessor = new SimpleFileMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND)) {
			// 命令处理
			messageProcessor = new ExecCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_FILE_OPEN)) {
			// 文件接收处理
			messageProcessor = new OpenFileCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_FILE_WRITE)) {
			// 文件接收开始
			messageProcessor = new TransFileMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_FILE_CLOSE)) {
			// 文件接收结束
			messageProcessor = new CloseFileCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SSH_OPEN)) {
			// 开启agent到服务器ssh
			messageProcessor = new OpenSSHCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SSH_CLOSE)) {
			// 关闭agent到服务器ssh
			messageProcessor = new CloseSSHCommandMessageProcessor();
		} else {
			messageProcessor = new NoCommandProcessor();
		}
		return messageProcessor;
	}
}
