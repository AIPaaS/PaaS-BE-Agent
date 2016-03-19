package com.ai.platform.agent.web.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.entity.SimpleCommandResInfo;
import com.ai.platform.agent.entity.SimpleFileResInfo;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.ai.platform.agent.util.ResultUtil;
import com.ai.platform.agent.web.util.AgentWebConstants;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;

@Service
public class SimpCommandSer {

	public static Logger logger = LogManager.getLogger(SimpCommandSer.class);

	private AuthChannelInfo getAuthChannelInfo(SimpleCommandReqInfo commandInfo) throws Exception {

		String aid = commandInfo.getAid();
		String command = commandInfo.getCommand();
		String key = aid;

		if (Strings.isBlank(aid)) {
			throw new Exception("***input Agent id");
		}
		
		if (Strings.isBlank(command)) {
			throw new Exception("***input command id");
		}

		if (!ChannelCollectionUtil.ctxMap.containsKey(key)) {
			throw new Exception("***client is not connected");
		}

		AuthChannelInfo channel = ChannelCollectionUtil.ctxMap.get(key);
		if (!channel.getCtx().channel().isActive()) {
			throw new Exception("***client is not active");
		}
		return channel;
	}

	/***
	 * 执行命令
	 * 
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	public String execCommand(SimpleCommandReqInfo commandInfo) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(commandInfo);
		byte[] execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND,
				JSON.toJSONString(commandInfo).getBytes());
		channel.getCtx().channel().writeAndFlush(execCommandArray);

		String key = commandInfo.getKey();

		int times = 1;

		while (!ResultUtil.SIMP_COMMAND_MSG_MAP.containsKey(key)) {
			Thread.sleep(AgentWebConstants.retrySleepTime);
			if (AgentWebConstants.timeOutSec < times * AgentWebConstants.retrySleepTime) {
				break;
			}
			times++;
		}

		SimpleCommandResInfo reMsg = null;
		if (ResultUtil.SIMP_COMMAND_MSG_MAP.containsKey(key)) {
			SimpleCommandReqInfo result = ResultUtil.SIMP_COMMAND_MSG_MAP.get(key);
			reMsg = new SimpleCommandResInfo(result.getCode(), result.getMsg());
			ResultUtil.SIMP_COMMAND_MSG_MAP.remove(key);
		} else {
			reMsg = new SimpleCommandResInfo(ResultCodeConstants.FAIL, "连接超时");
		}

		return JSON.toJSONString(reMsg);
	}

}
