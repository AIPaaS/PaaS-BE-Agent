package com.ai.platform.agent.client.incoming.processor.file;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.processor.AbstractSimpleFileProcessor;
import com.ai.platform.agent.entity.SimpleFileReqInfo;
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
public class SimpleFileMessageProcessor extends AbstractSimpleFileProcessor {

	public static Logger logger = LogManager.getLogger(SimpleFileMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		SimpleFileReqInfo msgInfo = JSON.parseObject(message, SimpleFileReqInfo.class);

		logger.info("服务端发来指令，内容：{}", super.command.getMessage());

		try {
			// 保存文件。目前默认覆盖，以后可扩展为追加，删除……
			File filePath = new File(msgInfo.getPath());
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			File file = new File(filePath, msgInfo.getFileName());
			FileOutputStream o = new FileOutputStream(file);
			o.write(msgInfo.getContent().getBytes("UTF-8"));
			o.flush();
			o.close();

			msgInfo.setCode("0");
			msgInfo.setMsg("执行成功");
			logger.info("执行结果：{}", msgInfo);

		} catch (Exception e) {
			logger.error("执行命令发生异常", e);
			msgInfo.setCode("1");
			msgInfo.setMsg("写入文件失败");
		} finally{
			byte[] contentArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SIMP_FILE_COMMAND,
					JSON.toJSONString(msgInfo).getBytes());
			ctx.channel().writeAndFlush(contentArray);
		}

	}
}
