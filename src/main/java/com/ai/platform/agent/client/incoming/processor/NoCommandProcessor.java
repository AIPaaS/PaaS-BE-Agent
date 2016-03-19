package com.ai.platform.agent.client.incoming.processor;

import com.ai.platform.agent.client.entity.SimpleCommandPacket;
import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;

public class NoCommandProcessor implements IMessageProcessor {
	
	protected SimpleCommandPacket command = new SimpleCommandPacket();

	@Override
	public void init(byte[] msg) throws MessageParserException {
		command.init(msg);
	}

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		
		SimpleCommandReqInfo msgInfo = new SimpleCommandReqInfo();
		msgInfo.setCode("1");
		msgInfo.setMsg("未知指令");
		
		byte[] contentArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SIMP_FILE_COMMAND,
				JSON.toJSONString(msgInfo).getBytes());
		ctx.channel().writeAndFlush(contentArray);
	}
	
}
