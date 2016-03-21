package com.ai.platform.agent.server.incoming.processor;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;
import com.ai.platform.agent.util.ResultUtil;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;

/***
 * 用于处理客户连接后的身份验证信息处理器
 *
 */
public class SimpleCommandMessageProcessor implements IMessageProcessor {

	private String message;

	static Logger logger = LogManager.getLogger(SimpleCommandMessageProcessor.class);

	@Override
	public void init(byte[] msg) throws MessageParserException {
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("处理客户端命令执行结果转换异常,转义信息：{}", msg);
			throw new MessageParserException("parse authInfo msg byte[] to string failure.", e);
		}
	}

	@Override
	public void proc(final ChannelHandlerContext ctx) {
		SimpleCommandReqInfo reciveMsg = JSONObject.parseObject(this.message, SimpleCommandReqInfo.class);
		logger.info("Receive client response info:{}", reciveMsg);
		ResultUtil.SIMP_COMMAND_MSG_MAP.put(reciveMsg.getKey(), reciveMsg);
	}
}
