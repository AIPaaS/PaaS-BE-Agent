package com.ai.platform.agent.exception;

/**
 * Agent Server 全局异常
 * @author wusheng
 *
 */
public class AgentServerException extends Exception {
	private static final long serialVersionUID = 6050485630751883906L;

	public AgentServerException(String message){
		super(message);
	}
	
	public AgentServerException(String message, Exception cause){
		super(message,cause);
	}
}
