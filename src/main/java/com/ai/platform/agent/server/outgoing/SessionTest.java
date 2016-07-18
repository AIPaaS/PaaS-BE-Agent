package com.ai.platform.agent.server.outgoing;

import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.UuidUtil;

import com.ai.platform.agent.entity.AgentSSHInfo;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSONObject;

public class SessionTest {

	public static Logger logger = LogManager.getLogger(SessionTest.class);

	public void startThread() {
		try {
			logger.info("启动模拟客户端，10秒后执行");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new Thread() {
			@SuppressWarnings("static-access")
			public void run() {
				while (true) {
					try {
						if (ChannelCollectionUtil.ctxMap.size() == 0) {
							logger.info("未发现客户端连接");
						}
						UUID id = UuidUtil.getTimeBasedUuid();
						String uid = id.randomUUID().toString().replaceAll("-", "");
						String sid = id.randomUUID().toString().replaceAll("-", "");
						for (Entry<String, AuthChannelInfo> tmpEntry : ChannelCollectionUtil.ctxMap.entrySet()) {

							if (!tmpEntry.getValue().getCtx().channel().isActive()) {
								ChannelCollectionUtil.ctxMap.remove(tmpEntry.getKey());
								continue;
							}

							AgentSSHInfo sshInfo = new AgentSSHInfo();
							sshInfo.setHost("10.1.235.197");
							sshInfo.setUserName("swuser01");
							sshInfo.setPassword("swuser01@123");
							String content = JSONObject.toJSONString(sshInfo);
							logger.info("服务端ssh到客户端，信息为：{}", sshInfo);

							byte[] openCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SSH_OPEN,
									uid.getBytes(), sid.getBytes(), content.getBytes());
							tmpEntry.getValue().getCtx().channel().writeAndFlush(openCommandArray);
							
							logger.info("2秒后发送执行命令");
							Thread.sleep(2000);
							//发送命令
							
							for(int i=0;i<100000; i++){
								String command = "cd redis-3.0.5 \n";
								byte[] execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
								
								command = "cd src \n";
								execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
								
								command = "pwd \n";
								execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
								
								command = "cd redis-3.0.5 \n";
								execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
								
								command = "cd src \n";
								execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
								
								command = "pwd \n";
								execCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_EXEC_COMMAND,
										uid.getBytes(), sid.getBytes(), command.getBytes());
								tmpEntry.getValue().getCtx().channel().writeAndFlush(execCommandArray);
							}
							
							// 结果回调
//							logger.info("2秒后接收执行命令结果");
//							Thread.sleep(2000);
							//接收命令执行结果
							
							logger.info("2秒后关闭会话");
							Thread.sleep(10000);
							//关闭命令
							byte[] closeCommandArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SSH_CLOSE,
									uid.getBytes(), sid.getBytes(), content.getBytes());
							tmpEntry.getValue().getCtx().channel().writeAndFlush(closeCommandArray);
							
						}
						sleep(5000);
					} catch (InterruptedException e) {
						System.out.println("sleep 失败");
					}
				}
			}
		}.start();
	}
}
