package com.ai.platform.agent.entity;

import com.ai.platform.agent.entity.HostInfo;

public class ShellCommandMVO {
	
	private HostInfo hostInfo;

	private String aid;

	private String uid;

	private String sid;
	
	private String command;
	
	private String callBackType;
	
	private String callBackContent;

	public HostInfo getHostInfo() {
		return hostInfo;
	}

	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCallBackType() {
		return callBackType;
	}

	public void setCallBackType(String callBackType) {
		this.callBackType = callBackType;
	}

	public String getCallBackContent() {
		return callBackContent;
	}

	public void setCallBackContent(String callBackContent) {
		this.callBackContent = callBackContent;
	}

	@Override
	public String toString() {
		return "ShellCommandMVO [hostInfo=" + hostInfo + ", aid=" + aid + ", uid=" + uid + ", sid=" + sid + ", command="
				+ command + ", callBackType=" + callBackType + ", callBackContent=" + callBackContent + "]";
	}
	
}
