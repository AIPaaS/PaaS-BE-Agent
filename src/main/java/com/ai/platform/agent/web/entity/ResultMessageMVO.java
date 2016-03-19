package com.ai.platform.agent.web.entity;

public class ResultMessageMVO {
	
	private String resultCode;
	
	private String msg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ResultMessageMVO(String resultCode, String msg) {
		super();
		this.resultCode = resultCode;
		this.msg = msg;
	}
	
}
