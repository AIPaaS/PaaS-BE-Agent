package com.ai.platform.agent.entity;

/***
 * 记录os服务器相关信息
 * @author Administrator
 *
 */
public class OSServerInfo {
	
	private String ip;
	
	private String cid;

	private String name;

	private String common;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

}
