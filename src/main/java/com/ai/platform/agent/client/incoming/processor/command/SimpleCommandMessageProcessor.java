package com.ai.platform.agent.client.incoming.processor.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.processor.AbstractSimpleCommandProcessor;
import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的命令处理
 * 
 * @author Administrator
 *
 */
public class SimpleCommandMessageProcessor extends
		AbstractSimpleCommandProcessor {

	public static Logger logger = LogManager
			.getLogger(SimpleCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		SimpleCommandReqInfo msgInfo = JSON.parseObject(message,
				SimpleCommandReqInfo.class);

		logger.info("服务端发来指令，内容：{}", super.command.getMessage());

		// 下面应该放到一个线程中执行
		try {

			Process pro = Runtime.getRuntime()
					.exec(new String[] { "/bin/sh", "-l", "-c",
							msgInfo.getCommand() });
			@SuppressWarnings("unused")
			int exitVal = pro.waitFor();

			StringBuffer stdout = new StringBuffer("");
			StringBuffer stderr = new StringBuffer("");

			JSONObject info = new JSONObject();

			InputStream stdIn = pro.getInputStream();
			InputStream errIn = pro.getErrorStream();
			BufferedReader stdRead = new BufferedReader(new InputStreamReader(
					stdIn, "UTF-8"));
			String line = null;
			while ((line = stdRead.readLine()) != null) {
				stdout.append(line + System.lineSeparator());
			}

			info.put("stdout", stdout.toString());

			BufferedReader errRead = new BufferedReader(new InputStreamReader(
					errIn, "UTF-8"));
			String errLine = null;
			while ((errLine = errRead.readLine()) != null) {
				stderr.append(errLine + System.lineSeparator());
			}

			String errStr = stderr.toString();
			info.put("stderr", errStr);

			// 如果异常输出有值，则为异常，code为999999
			if (!Strings.isNullOrEmpty(errStr)) {
				msgInfo.setCode("999999");
			} else {
				msgInfo.setCode("0");
			}

			msgInfo.setMsg(info.toJSONString());

			logger.info("执行结果：{}", msgInfo);

		} catch (Exception e) {
			logger.error("执行命令发生异常", e);
			msgInfo.setCode("1");
			msgInfo.setMsg("写入文件失败");
		} finally {
			byte[] contentArray = ByteArrayUtil.mergeByteArray(
					AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND, JSON
							.toJSONString(msgInfo).getBytes());
			ctx.channel().writeAndFlush(contentArray);
		}

	}
}
