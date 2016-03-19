package com.ai.platform.agent.entity;

public class Server2ClientMsgMVO {
	
	private String bussInfo;
	
	private ShellCommandMVO commandInfo;
	
	private String responseContent;

	public String getBussInfo() {
		return bussInfo;
	}

	public void setBussInfo(String bussInfo) {
		this.bussInfo = bussInfo;
	}

	public ShellCommandMVO getCommandInfo() {
		return commandInfo;
	}

	public void setCommandInfo(ShellCommandMVO commandInfo) {
		this.commandInfo = commandInfo;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	@Override
	public String toString() {
		return "Server2ClientMsgMVO [bussMsg=" + bussInfo + ", commandInfo=" + commandInfo + ", responseContent="
				+ responseContent + "]";
	}

}
