package com.ai.platform.agent.web.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.ai.platform.agent.entity.SimpleFileReqInfo;
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
public class SimpFileCommandSer {

	public static Logger logger = LogManager.getLogger(SimpFileCommandSer.class);

	private AuthChannelInfo getAuthChannelInfo(SimpleFileReqInfo commandInfo) throws Exception {

		String key = commandInfo.getKey();
		String aid = commandInfo.getAid();
		String content = commandInfo.getContent();
		String path = commandInfo.getPath();
		String fileName = commandInfo.getFileName();
		String aidKey = aid;

		if (Strings.isBlank(aid)) {
			throw new Exception("*** input Agent id");
		}

		if (Strings.isBlank(content)) {
			throw new Exception("*** input content id");
		}

		if (Strings.isBlank(path)) {
			throw new Exception("*** input path id");
		}

		if (Strings.isBlank(fileName)) {
			throw new Exception("*** input fileName id");
		}

		if (!ChannelCollectionUtil.ctxMap.containsKey(aidKey)) {
			throw new Exception("*** client is not connected");
		}

		AuthChannelInfo channel = ChannelCollectionUtil.ctxMap.get(aidKey);
		if (!channel.getCtx().channel().isActive()) {
			throw new Exception("*** client is not active");
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
	public String uploadFile(SimpleFileReqInfo commandInfo) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(commandInfo);
		byte[] execCommandArray = ByteArrayUtil.mergeByteArray(
				AgentServerCommandConstant.PACKAGE_TYPE_SIMP_FILE_COMMAND, JSON.toJSONString(commandInfo).getBytes());
		channel.getCtx().channel().writeAndFlush(execCommandArray);

		String key = commandInfo.getKey();

		int times = 1;

		while (!ResultUtil.SIMP_FILE_MSG_MAP.containsKey(key)) {
			Thread.sleep(AgentWebConstants.retrySleepTime);
			if (AgentWebConstants.timeOutSec < times * AgentWebConstants.retrySleepTime) {
				break;
			}
			times++;
		}

		SimpleFileResInfo reMsg = null;
		if (ResultUtil.SIMP_FILE_MSG_MAP.containsKey(key)) {
			SimpleFileReqInfo result = ResultUtil.SIMP_FILE_MSG_MAP.get(key);
			reMsg = new SimpleFileResInfo(result.getCode(), result.getMsg());
			ResultUtil.SIMP_FILE_MSG_MAP.remove(key);
		} else {
			reMsg = new SimpleFileResInfo(ResultCodeConstants.FAIL, "连接超时");
		}

		return JSON.toJSONString(reMsg);
	}

}
