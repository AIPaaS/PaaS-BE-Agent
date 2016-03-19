package com.ai.platform.agent.processor;

import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;

import io.netty.channel.ChannelHandlerContext;

public interface IMessageProcessor {
	
	public void init(byte[] msg) throws MessageParserException;
	
	public void proc(ChannelHandlerContext ctx) throws AgentServerException;
}
