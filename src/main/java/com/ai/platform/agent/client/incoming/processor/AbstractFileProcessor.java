package com.ai.platform.agent.client.incoming.processor;

import com.ai.platform.agent.client.entity.FilePacket;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;

import io.netty.channel.ChannelHandlerContext;

public class AbstractFileProcessor implements IMessageProcessor {
	
	protected FilePacket command = new FilePacket();

	@Override
	public void init(byte[] msg) throws MessageParserException {
		command.init(msg);
	}

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
	}
	
}
