package com.ai.platform.agent.common;

import com.ai.platform.agent.exception.AgentServerException;

public interface IDataPacket {

	/***
	 * 数据包格式为前两个字节是类型，后面的是包体
	 * @param msg
	 * @return
	 * @throws AgentServerException
	 */
	public byte[] genDataPacket(byte[] msg,String agentClientInfo) throws AgentServerException;
	
}
