package com.ai.platform.agent.util;

/***
 * agent服务端向客户端发送指令列表
 * 
 * @author Administrator
 *
 */
public class AgentServerCommandConstant {

	// 1期指令列表（以下都是Agent服务端通过Agent客户端操作）
	// * 1.1、开启远端服务器会话指令（ssh）
	// * 1.2、关闭远端服务端器（指定）会话指令
	// * 1.3、在远端服务器上执行命令、脚本
	// * 1.4、上传文件到远端服务器指定目录
	
	/** 服务端向客户端发送开启ssh命令 服务端向客户端发送 */
	public static final byte[] PACKAGE_TYPE_SSH_OPEN = { 4, 0 };
	/** 服务端向客户端发送关闭ssh命令 服务端向客户端发送 */
	public static final byte[] PACKAGE_TYPE_SSH_CLOSE = { 4, 1 };
	/** 字符类型的命令数据包 服务端向客户端发送 */
	public static final byte[] PACKAGE_TYPE_EXEC_COMMAND = { 2, 0 };
	/** 文件的数据包 开始包 */
	public static final byte[] PACKAGE_TYPE_FILE_OPEN = { 3, 0 };
	/** 文件的数据包 数据包 */
	public static final byte[] PACKAGE_TYPE_FILE_WRITE = { 3, 1 };
	/** 文件的数据包 结束包 */
	public static final byte[] PACKAGE_TYPE_FILE_CLOSE = { 3, 2 };
	/** 一次性命令 */
	public static final byte[] PACKAGE_TYPE_SIMP_COMMAND = { 5, 0 };
	/** 简单传输文件命令 */
	public static final byte[] PACKAGE_TYPE_SIMP_FILE_COMMAND = { 6, 0 };
}
