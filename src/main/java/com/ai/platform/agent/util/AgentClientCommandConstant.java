package com.ai.platform.agent.util;

public class AgentClientCommandConstant {
	
	/** 客户端发起的包 */
	/** 客户端与服务器建立连接后发送的身份验证包 */
	public static final byte[] PACKAGE_TYPE_CLIENT_AUTH = { 0, 0 };
	/** 常规心跳包 客户端向服务器发送 */
	public static final byte[] PACKAGE_TYPE_KEEP_LIVE = { 1, 0 };
	/** 字符类型的响应 数据包 客户端向服务器发送 */
	public static final byte[] PACKAGE_TYPE_COMMAND_RESPONSE = { 9, 0 };
}
