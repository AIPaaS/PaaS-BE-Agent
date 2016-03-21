package com.ai.platform.agent.entity;

public class AgentConfigInfo {
	
	private String agentServerIp;
	private String agentServerPort;
	private String agentTimeOut;

	private String agentClientInfo;
	private String agentFileStoragePath;

	public String getAgentServerIp() {
		return agentServerIp;
	}

	public void setAgentServerIp(String agentServerIp) {
		this.agentServerIp = agentServerIp;
	}

	public String getAgentServerPort() {
		return agentServerPort;
	}

	public void setAgentServerPort(String agentServerPort) {
		this.agentServerPort = agentServerPort;
	}

	public String getAgentTimeOut() {
		return agentTimeOut;
	}

	public void setAgentTimeOut(String agentTimeOut) {
		this.agentTimeOut = agentTimeOut;
	}

	public String getAgentClientInfo() {
		return agentClientInfo;
	}

	public void setAgentClientInfo(String agentClientInfo) {
		this.agentClientInfo = agentClientInfo;
	}

	public String getAgentFileStoragePath() {
		return agentFileStoragePath;
	}

	public void setAgentFileStoragePath(String agentFileStoragePath) {
		this.agentFileStoragePath = agentFileStoragePath;
	}
}
