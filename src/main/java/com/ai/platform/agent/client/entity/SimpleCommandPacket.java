package com.ai.platform.agent.client.entity;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.MessageParserException;

public class SimpleCommandPacket {

	public static Logger logger = LogManager.getLogger(SimpleCommandPacket.class);

	private String message;

	public void init(byte[] msg) throws MessageParserException {
		
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("消息体 转义异常，data内容：{}", msg);
			throw new MessageParserException("parse byte[] to string failure.", e);
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
