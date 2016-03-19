package com.ai.platform.agent.server.incoming.handler;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;
import com.ai.platform.agent.server.incoming.processor.AuthMessageProcessor;
import com.ai.platform.agent.server.incoming.processor.CommandMessageProcessor;
import com.ai.platform.agent.server.incoming.processor.SimpleCommandMessageProcessor;
import com.ai.platform.agent.server.incoming.processor.SimpleFileMessageProcessor;
import com.ai.platform.agent.util.AgentClientCommandConstant;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.AgentServerCommandConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IncomingMessageHandler extends SimpleChannelInboundHandler<byte[]> {

	static Logger logger = LogManager.getLogger(IncomingMessageHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("接入文件内容：[{}]", msg);
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
		IMessageProcessor messageProcessor = null;

		/** 按照频次，把经常用的放在最上面 */
		if (Arrays.equals(type, AgentClientCommandConstant.PACKAGE_TYPE_KEEP_LIVE)) {
			// 常规心跳包
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND)) {
			// 接收文件
			messageProcessor = new SimpleCommandMessageProcessor();
		} else if (Arrays.equals(type, AgentServerCommandConstant.PACKAGE_TYPE_SIMP_FILE_COMMAND)) {
			// 接收文件
			messageProcessor = new SimpleFileMessageProcessor();
		} else if (Arrays.equals(type, AgentClientCommandConstant.PACKAGE_TYPE_COMMAND_RESPONSE)) {
			// 命令响应包
			messageProcessor = new CommandMessageProcessor();
		} else if (Arrays.equals(type, AgentClientCommandConstant.PACKAGE_TYPE_CLIENT_AUTH)) {
			// 身份验证
			messageProcessor = new AuthMessageProcessor();
		} else {
			logger.error("未定义的操作类型：[{}]", type);
			throw new AgentServerException("未定义的操作类型：[{" + type.toString() + "}]");
		}
		return messageProcessor;
	}
}
