package com.ai.platform.agent.client.incoming.processor.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.processor.AbstractSimpleCommandProcessor;
import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的命令处理
 * 
 * @author Administrator
 *
 */
public class ExecCommandMessageProcessor extends AbstractSimpleCommandProcessor {

	public static Logger logger = LogManager.getLogger(ExecCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		SimpleCommandReqInfo msgInfo = JSON.parseObject(message, SimpleCommandReqInfo.class);

		String command = msgInfo.getCommand();
		logger.info("服务端发来指令，内容：{}", super.command.getMessage());

		try {

			// 响应给服务端
			// 组装执行命令结果报文
			msgInfo.setMsg("-------------------");
			logger.info("执行结果：{}", msgInfo);

			byte[] contentArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND,
					JSON.toJSONString(msgInfo).getBytes());
			ctx.channel().writeAndFlush(contentArray);
		} catch (Exception e) {
			logger.error("执行命令发生异常", e);
			throw new AgentServerException("执行命令发生异常");
		}

	}
}
