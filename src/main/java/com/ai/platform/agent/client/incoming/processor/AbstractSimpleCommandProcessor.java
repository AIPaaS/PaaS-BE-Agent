package com.ai.platform.agent.client.incoming.processor;

import com.ai.platform.agent.client.entity.SimpleCommandPacket;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;

import io.netty.channel.ChannelHandlerContext;

public class AbstractSimpleCommandProcessor implements IMessageProcessor {
	
	protected SimpleCommandPacket command = new SimpleCommandPacket();

	@Override
	public void init(byte[] msg) throws MessageParserException {
		command.init(msg);
	}

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
	}
	
}
