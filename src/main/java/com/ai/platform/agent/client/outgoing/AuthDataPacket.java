package com.ai.platform.agent.client.outgoing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.common.IDataPacket;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentClientCommandConstant;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

public class AuthDataPacket implements IDataPacket {

	private JSONObject agentClientJson = null;

	public static Logger logger = LogManager.getLogger(AuthDataPacket.class);

	@Override
	public byte[] genDataPacket(byte[] msg,String agentClientInfo) throws AgentServerException {
		//
		//String agentClientInfo = AgentClientInit.clientConstant.get(AgentConstant.AGENT_CLIENT_INFO);
		if (Strings.isNullOrEmpty(agentClientInfo)) {
			logger.error("配置文件中缺少{}信息", AgentConstant.AGENT_CLIENT_INFO);
			throw new AgentServerException("配置文件中缺少" + AgentConstant.AGENT_CLIENT_INFO + "信息");
		}
		try {
			agentClientJson = JSONObject.parseObject(agentClientInfo);
		} catch (Exception e) {
			logger.error("配置文件中{}信息不符合JSON格式", AgentConstant.AGENT_CLIENT_INFO);
			throw new AgentServerException("配置文件中" + AgentConstant.AGENT_CLIENT_INFO + "信息不符合JSON格式", e);
		}

		byte[] authPacket = ByteArrayUtil.mergeByteArray(AgentClientCommandConstant.PACKAGE_TYPE_CLIENT_AUTH, agentClientInfo.getBytes());

		return authPacket;
	}

	public JSONObject getAuthJson() {
		return agentClientJson;
	}

	public void setAuthJson(JSONObject authJson) {
		this.agentClientJson = authJson;
	}

}
