package com.ai.platform.agent.entity;

public class NettyClientResponseMsgMVO {
	
	private String bussInfo;
	
	private String code;
	
	private String msg;

	public String getBussInfo() {
		return bussInfo;
	}

	public void setBussInfo(String bussInfo) {
		this.bussInfo = bussInfo;
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
		return "NettyClientResponseMsgMVO [bussInfo=" + bussInfo + ", code=" + code + ", msg=" + msg + "]";
	}

}
