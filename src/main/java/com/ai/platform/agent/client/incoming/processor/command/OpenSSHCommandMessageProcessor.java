package com.ai.platform.agent.client.incoming.processor.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserShellChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractCommandProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.entity.AgentSSHInfo;
import com.ai.platform.agent.entity.UserSessionInfo;
import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端开启会话命令处理
 * 
 * @author Administrator
 *
 */
public class OpenSSHCommandMessageProcessor extends AbstractCommandProcessor {

	public static Logger logger = LogManager.getLogger(OpenSSHCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) {
		String message = super.command.getMessage();
		logger.info("服务端发来指令，内容：{}", message);
		String uid = super.command.getUid();
		String sid = super.command.getSid();

		String key = sid;
		if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {

			UserShellChannelInfo usci = new UserShellChannelInfo();
			UserSessionInfo usi = new UserSessionInfo();
			usi.setUid(uid);
			usi.setSid(sid);

			AgentSSHInfo sshInfo = JSON.parseObject(message, AgentSSHInfo.class);

			usci.firstSSHAction(sshInfo);

			ShellChannelCollectionUtil.userChannelMap.put(key, usci);
		} else {
			logger.info("当前 key:{}已经登录", sid);
		}

	}
}
