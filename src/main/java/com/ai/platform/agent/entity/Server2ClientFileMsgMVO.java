package com.ai.platform.agent.entity;

public class Server2ClientFileMsgMVO {
	
	private String bussInfo;
	
	private AgentSftpInfo transFileInfo;
	
	private String responseContent;

	public String getBussInfo() {
		return bussInfo;
	}

	public AgentSftpInfo getTransFileInfo() {
		return transFileInfo;
	}

	public void setTransFileInfo(AgentSftpInfo transFileInfo) {
		this.transFileInfo = transFileInfo;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public void setBussInfo(String bussInfo) {
		this.bussInfo = bussInfo;
	}

	@Override
	public String toString() {
		return "Server2ClientFileMsgMVO [bussMsg=" + bussInfo + ", transFileInfo=" + transFileInfo + ", responseContent="
				+ responseContent + "]";
	}


}
