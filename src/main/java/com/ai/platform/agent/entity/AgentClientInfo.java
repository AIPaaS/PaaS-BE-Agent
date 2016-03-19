package com.ai.platform.agent.entity;

import java.util.ArrayList;
import java.util.List;

public class AgentClientInfo {

	private String aid;

	private String name;

	private String common;

	/** agent可管理服务器列表 */
	private List<OSServerInfo> computerList = new ArrayList<OSServerInfo>();

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
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

	public List<OSServerInfo> getComputerList() {
		return computerList;
	}

	public void setComputerList(List<OSServerInfo> computerList) {
		this.computerList = computerList;
	}

}
