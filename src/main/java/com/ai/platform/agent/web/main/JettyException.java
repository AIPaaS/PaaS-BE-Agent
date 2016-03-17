package com.ai.platform.agent.web.main;

public class JettyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JettyException() {
		super();
	}
	
	public JettyException(String message) {
		super(message);
	}
	
	public JettyException(Throwable cause) {
		super(cause);
	}
	
	public JettyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}


