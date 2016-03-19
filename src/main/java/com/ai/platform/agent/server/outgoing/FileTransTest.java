package com.ai.platform.agent.server.outgoing;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.UuidUtil;

import com.ai.platform.agent.entity.AgentSSHInfo;
import com.ai.platform.agent.entity.AgentSftpInfo;
import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.server.entity.AuthChannelInfo;
import com.ai.platform.agent.server.util.ChannelCollectionUtil;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FileTransTest {

	public static Logger logger = LogManager.getLogger(FileTransTest.class);

	public void startThread() {
		try {
			logger.info("启动模拟客户端，10秒后执行");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new Thread() {
			public void run() {
				try {
					while (true) {
						if (ChannelCollectionUtil.ctxMap.size() == 0) {
							logger.info("未发现客户端连接");
						}
						UUID id = UuidUtil.getTimeBasedUuid();
						String uid = id.randomUUID().toString().replaceAll("-", "");
						String sid = id.randomUUID().toString().replaceAll("-", "");
						String fid = id.randomUUID().toString().replaceAll("-", "");

						JSch jsch = new JSch();
						long startTime = System.currentTimeMillis();

						String host = "10.1.235.197";
						String user = "swuser01";
						final Session session = jsch.getSession(user, host, 22);

						String passwd = "swuser01@123";
						session.setPassword(passwd);

						session.setConfig("StrictHostKeyChecking", "no");
						session.connect();

						Channel channel = session.openChannel("sftp");
						channel.connect();
						ChannelSftp sftp = (ChannelSftp) channel;

						AgentSftpInfo sftpInfo = new AgentSftpInfo();
						HostFileInfo srcFileInfo = new HostFileInfo();
						HostFileInfo descfileInfo = new HostFileInfo();
						sftpInfo.setDescFileSftpInfo(descfileInfo);
						sftpInfo.setSrcFileSftpInfo(srcFileInfo);

						srcFileInfo.setHost("10.1.235.197");
						srcFileInfo.setUserName("swuser01");
						srcFileInfo.setPassword("swuser01@123");
						srcFileInfo.setPath("/aifs01/users/swuser01");
						srcFileInfo.setFileName("apache-tomcat-8.0.26.zip");

						descfileInfo.setHost("10.1.235.197");
						descfileInfo.setUserName("swuser01");
						descfileInfo.setPassword("swuser01@123");
						descfileInfo.setPath("/aifs01/users/swuser01/test_dir");
						descfileInfo.setFileName("apache-tomcat-8.0.26.zip");

						sftp.setBulkRequests(32);

						sftp.cd(sftpInfo.getSrcFileSftpInfo().getPath());
						InputStream instream = sftp.get(sftpInfo.getSrcFileSftpInfo().getFileName());

						byte b[] = new byte[1024 * 100];

						for (Entry<String, AuthChannelInfo> tmpEntry : ChannelCollectionUtil.ctxMap.entrySet()) {

							if (!tmpEntry.getValue().getCtx().channel().isActive()) {
								ChannelCollectionUtil.ctxMap.remove(tmpEntry.getKey());
								continue;
							}

							// 开始发送文件
							String fileContent = JSONObject.toJSONString(descfileInfo);
							logger.info("服务端ssh到客户端，信息为：{}", fileContent);

							byte[] openFileCommandArray = ByteArrayUtil.mergeByteArray(
									AgentServerCommandConstant.PACKAGE_TYPE_FILE_OPEN, uid.getBytes(), sid.getBytes(),
									fid.getBytes(), fileContent.getBytes());
							tmpEntry.getValue().getCtx().channel().writeAndFlush(openFileCommandArray);

							logger.info("2秒后[传输文件内容]指令");
							Thread.sleep(1000);

							// 发送传输文件命令
							byte[] fileContentArray;
							int i = 0;
							int n;
							int m = 0;
							while ((n = instream.read(b)) != -1) {
								byte[] tmpBytes = new byte[n];
								System.out.println("---" + n);
								System.arraycopy(b, 0, tmpBytes, 0, n);

								for (int t = 0; t < 19 && (m = instream.read(b)) != -1; t++) {
									Thread.sleep(100);
									byte[] tmpBytes1 = new byte[m];
									System.out.println("---" + m);
									System.arraycopy(b, 0, tmpBytes1, 0, m);
									tmpBytes = ByteArrayUtil.mergeByteArray(tmpBytes, tmpBytes1);
								}

								fileContentArray = ByteArrayUtil.mergeByteArray(
										AgentServerCommandConstant.PACKAGE_TYPE_FILE_WRITE, uid.getBytes(),
										sid.getBytes(), fid.getBytes(), tmpBytes);
								tmpEntry.getValue().getCtx().channel().writeAndFlush(fileContentArray);
								System.out.println("======" + tmpBytes.length + "   " + i++);
							}

							instream.close();

							// 关闭文件指令
							logger.info("2秒后发送[关闭文件]指令");
							Thread.sleep(10000);
							byte[] closeFileCommandArray = ByteArrayUtil.mergeByteArray(
									AgentServerCommandConstant.PACKAGE_TYPE_FILE_CLOSE, uid.getBytes(), sid.getBytes(),
									fid.getBytes(), "1".getBytes());
							tmpEntry.getValue().getCtx().channel().writeAndFlush(closeFileCommandArray);

						}

						logger.info("线程执行结束");
						sleep(5000000);
					}
				} catch (Exception e) {
					System.out.println("sleep 失败");
					e.printStackTrace();
				}
			}
		}.start();
	}
}
