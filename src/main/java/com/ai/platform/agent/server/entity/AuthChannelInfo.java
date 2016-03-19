package com.ai.platform.agent.server.entity;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;

public class AuthChannelInfo {
	
	private JSONObject authJson;
	
	private ChannelHandlerContext ctx;

	public JSONObject getAuthJson() {
		return authJson;
	}

	public void setAuthJson(JSONObject authJson) {
		this.authJson = authJson;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
}
