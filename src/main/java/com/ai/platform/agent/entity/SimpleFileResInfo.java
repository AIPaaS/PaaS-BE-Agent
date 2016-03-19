package com.ai.platform.agent.entity;

public class SimpleFileResInfo {

private String code;
	
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SimpleFileResInfo [code=" + code + ", msg=" + msg + "]";
	}

	public SimpleFileResInfo(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

}
