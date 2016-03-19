package com.ai.platform.agent.web.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.ai.platform.agent.entity.Server2ClientMsgMVO;
import com.ai.platform.agent.entity.ShellCommandMVO;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class ShellCommandSer {

	public static Logger logger = LogManager.getLogger(ShellCommandSer.class);

	private AuthChannelInfo getAuthChannelInfo(Server2ClientMsgMVO sendMsg) throws Exception {
		ShellCommandMVO commandInfo = sendMsg.getCommandInfo();

		String aid = commandInfo.getAid();
		String uid = commandInfo.getUid();
		String sid = commandInfo.getSid();
		String key = aid;

		if (Strings.isBlank(aid)) {
			throw new Exception("请指定Agent标识");
		}

		if (Strings.isBlank(uid)) {
			throw new Exception("请指定uid标识");
		}

		if (Strings.isBlank(sid)) {
			throw new Exception("请指定sid标识");
		}

		if (!ChannelCollectionUtil.ctxMap.containsKey(key)) {
			throw new Exception("指定的客户端未连接");
		}

		AuthChannelInfo channel = ChannelCollectionUtil.ctxMap.get(key);
		if (!channel.getCtx().channel().isActive()) {
			throw new Exception("指定的客户端未激活");
		}
		return channel;
	}

	/***
	 * 初始化会话
	 * 
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	public String initCommandSession(Server2ClientMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);
		ShellCommandMVO commandInfo = sendMsg.getCommandInfo();
		String content = JSONObject.toJSONString(commandInfo.getHostInfo());
		logger.info("服务端ssh到客户端，信息为：{}", commandInfo.getHostInfo());

		byte[] openCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SSH_OPEN,
				commandInfo.getUid().getBytes(), commandInfo.getSid().getBytes(), content.getBytes());
		channel.getCtx().channel().writeAndFlush(openCommandArray);

		ResultMessageMVO result = new ResultMessageMVO(ResultCodeConstants.OK, ResultCodeConstants.OK_MSG);
		return JSON.toJSONString(result);
	}

	/***
	 * 执行命令
	 * 
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	public String execCommand(Server2ClientMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);
		ShellCommandMVO commandInfo = sendMsg.getCommandInfo();
		byte[] execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
				commandInfo.getUid().getBytes(), commandInfo.getSid().getBytes(),
				JSON.toJSONString(sendMsg).getBytes());
		channel.getCtx().channel().writeAndFlush(execCommandArray);

		ResultMessageMVO result = new ResultMessageMVO(ResultCodeConstants.OK, ResultCodeConstants.OK_MSG);
		return JSON.toJSONString(result);
	}

	/***
	 * 销毁会话
	 * 
	 * @param sendMsg
	 * @return
	 * @throws Exception
	 */
	public String destroyCommand(Server2ClientMsgMVO sendMsg) throws Exception {
		AuthChannelInfo channel = getAuthChannelInfo(sendMsg);
		ShellCommandMVO commandInfo = sendMsg.getCommandInfo();
		String content = "close session";

		byte[] closeCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SSH_CLOSE,
				commandInfo.getUid().getBytes(), commandInfo.getSid().getBytes(), content.getBytes());
		channel.getCtx().channel().writeAndFlush(closeCommandArray);

		ResultMessageMVO result = new ResultMessageMVO(ResultCodeConstants.OK, ResultCodeConstants.OK_MSG);
		return JSON.toJSONString(result);
	}

}
