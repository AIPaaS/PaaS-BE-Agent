package com.ai.platform.agent.entity;

public class SimpleCommandResInfo {

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
		return "SimpleCommandResInfo [code=" + code + ", msg=" + msg + "]";
	}

	public SimpleCommandResInfo(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

}
