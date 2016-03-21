package com.ai.platform.agent.entity;

import com.ai.platform.agent.util.ConfigInit;
import com.ai.platform.agent.util.MapBeanUtils;

public class AgentConfigInfoServer {
	private AgentConfigInfo agentConfigInfo;

	public AgentConfigInfoServer(){
		//
		this.agentConfigInfo = MapBeanUtils.map2Bean(ConfigInit.serverConstant, AgentConfigInfo.class);
		//
	}
	
	public AgentConfigInfo getAgentConfigInfo() {
		return agentConfigInfo;
	}

	public void setAgentConfigInfo(AgentConfigInfo agentConfigInfo) {
		this.agentConfigInfo = agentConfigInfo;
	}


}
