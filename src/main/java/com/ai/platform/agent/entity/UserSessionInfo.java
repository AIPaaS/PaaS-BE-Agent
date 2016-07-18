package com.ai.platform.agent.entity;

import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.ConfigInit;

public class UserSessionInfo {
	
	/** 用户标识 */
	private String uid;
	
	/** 会话(开启一个终端连接就是一个会话)标识 */
	private String sid;
	
	private long timeout;
	
	@SuppressWarnings("unused")
	private long startTime;
	
	public UserSessionInfo(){
		this.timeout = Long.valueOf(ConfigInit.serverConstant.get(AgentConstant.AGENT_TIMEOUT));
		this.startTime = System.currentTimeMillis();
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

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
