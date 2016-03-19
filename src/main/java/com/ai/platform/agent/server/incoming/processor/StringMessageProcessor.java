package com.ai.platform.agent.server.incoming.processor;

import java.io.UnsupportedEncodingException;

import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;

import io.netty.channel.ChannelHandlerContext;

/***
 * 处理客户执行完命令后的响应
 *
 */
public class StringMessageProcessor implements IMessageProcessor {
	private String message;

	@Override
	public void init(byte[] msg) throws MessageParserException {
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MessageParserException("parse msg byte[] to string failure.",
					e);
		}
	}

	@Override
	public void proc(final ChannelHandlerContext ctx) {
		System.out.println(message);
	}
}
