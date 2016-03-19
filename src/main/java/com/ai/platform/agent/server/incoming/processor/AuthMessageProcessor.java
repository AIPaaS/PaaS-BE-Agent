package com.ai.platform.agent.server.incoming.processor;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.processor.IMessageProcessor;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentConstant;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;

/***
 * 用于处理客户连接后的身份验证信息处理器
 *
 */
public class AuthMessageProcessor implements IMessageProcessor {

	private String message;

	static Logger logger = LogManager.getLogger(AuthMessageProcessor.class);

	@Override
	public void init(byte[] msg) throws MessageParserException {
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("处理客户端身份验证信息转换异常,转义信息：{}", msg);
			throw new MessageParserException("parse authInfo msg byte[] to string failure.", e);
		}
	}

	@Override
	public void proc(final ChannelHandlerContext ctx) {
		AuthChannelInfo authChannel = new AuthChannelInfo();
		JSONObject authJson = JSONObject.parseObject(this.message);
		String Pid = authJson.getString(AgentConstant.AUTH_JSON_AID);
		authChannel.setAuthJson(authJson);
		authChannel.setCtx(ctx);
		ChannelCollectionUtil.ctxMap.put(Pid, authChannel);
		logger.info("agent客户端[{}]上线,身份信息：[{}],channel信息：{}", authJson.getString(AgentConstant.CHANNEL_SHOW_KEY),
				authJson, ctx.channel().remoteAddress());
	}
}
