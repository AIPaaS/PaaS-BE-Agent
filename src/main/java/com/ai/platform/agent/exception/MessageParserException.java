package com.ai.platform.agent.exception;

public class MessageParserException extends AgentServerException {
	private static final long serialVersionUID = 5509483141617143718L;
	
	public MessageParserException(String message){
		super(message);
	}

	public MessageParserException(String message, Exception cause){
		super(message,cause);
	}
}
