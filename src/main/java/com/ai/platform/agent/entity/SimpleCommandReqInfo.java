package com.ai.platform.agent.entity;

public class SimpleCommandReqInfo {
	private String key;

	private String aid;

	private String command;
	
	private String code;
	
	private String msg;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

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
		return "SimpleCommandReqInfo [key=" + key + ", aid=" + aid + ", command=" + command + ", code=" + code
				+ ", msg=" + msg + "]";
	}

}
