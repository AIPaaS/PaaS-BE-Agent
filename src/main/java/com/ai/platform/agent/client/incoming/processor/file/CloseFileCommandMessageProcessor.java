package com.ai.platform.agent.client.incoming.processor.file;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractFileProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.entity.Server2ClientFileMsgMVO;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentClientCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端结束文件传输的命令处理
 * 
 * @author Administrator
 *
 */
public class CloseFileCommandMessageProcessor extends AbstractFileProcessor {

	public static Logger logger = LogManager.getLogger(CloseFileCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		logger.info("服务端发来指令，内容：{}", message);
		String sid = super.command.getSid();
		String fid = super.command.getFid();
		String key = sid;
		String fkey = fid;

		if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {
			throw new AgentServerException("当前会话容器未找到sid:{}的会话信息");
		}

		UserChannelInfo usci = ShellChannelCollectionUtil.userChannelMap.get(key);

//		usci.checkChannelIsOpen();

		try {
			OutputStream outputStream = usci.getOutstream();
			outputStream.flush();
			outputStream.close();

			Channel channel = usci.getChannel();
			channel.disconnect();

			Session session = usci.getSession();
			session.disconnect();

			Server2ClientFileMsgMVO msgInfo = JSON.parseObject(message, Server2ClientFileMsgMVO.class);
			
			msgInfo.setResponseContent("文件传输完成");
			
			byte[] contentArray = ByteArrayUtil.mergeByteArray(AgentClientCommandConstant.PACKAGE_TYPE_COMMAND_RESPONSE,
					JSON.toJSONString(msgInfo).getBytes());
			ctx.channel().writeAndFlush(contentArray);

		} catch (IOException e) {
			logger.error("文件写入失败", e);
			throw new AgentServerException("文件写入失败");
		}
	}
}
