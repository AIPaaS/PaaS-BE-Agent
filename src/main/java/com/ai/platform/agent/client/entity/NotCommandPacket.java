package com.ai.platform.agent.client.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.MessageParserException;

public class NotCommandPacket {

	public static Logger logger = LogManager.getLogger(NotCommandPacket.class);

	@SuppressWarnings("unused")
	private String message;

	public void init(byte[] msg) throws MessageParserException {
		
	}

}
