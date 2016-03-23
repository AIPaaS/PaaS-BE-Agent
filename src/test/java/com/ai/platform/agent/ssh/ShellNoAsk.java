package com.ai.platform.agent.ssh;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ShellNoAsk {
	public static void main(String[] arg) {

		try {
			JSch jsch = new JSch();

			String host = "10.1.235.197";
			String user = "swuser01";
			final Session session = jsch.getSession(user, host, 22);

			String passwd = "swuser01@123";
			session.setPassword(passwd);

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(30000); // making a connection with timeout.

			final Channel channel = session.openChannel("shell");
			
			try {
				// 创建sftp通信通道
				channel.connect(10000);

				// 发送需要执行的SHELL命令，需要用\n结尾，表示回车
				String shellCommand = "cd redis-3.0.5 \n pwd \n";
				// 获取输入流和输出流
				InputStream instream = channel.getInputStream();
				OutputStream outstream = channel.getOutputStream();

				outstream.write(shellCommand.getBytes());
				outstream.flush();
				new Thread() {
					public void run() {
						System.out.println("执行线程");
						while (true) {
							try {
								Thread.sleep(5000);
								System.out.println("线程sleep 10秒");
								
								Channel channel = session.openChannel("shell");
								channel.connect();
								// 发送需要执行的SHELL命令，需要用\n结尾，表示回车
								String shellCommand = "pwd \n cd redis-3.0.5 \n ls -al \n";
								// 获取输入流和输出流
								InputStream instream = channel.getInputStream();
								OutputStream outstream = channel.getOutputStream();
								
								outstream.write(shellCommand.getBytes());
								outstream.flush();
								
								Thread.sleep(3000);
								if (true) {
									byte[] data = new byte[instream.available()];
									int nLen = instream.read(data);

									if (nLen < 0) {
										throw new Exception("network error.");
									}

									// 转换输出结果并打印出来
									String temp = new String(data, 0, nLen, "UTF-8");
									System.out.println(temp);

								}
								outstream.close();
								instream.close();

							} catch (Exception e) {
								System.out.println("sleep 失败");
								e.printStackTrace();
							}

						}
					}
				}.start();

				// 获取命令执行的结果
				while (true) {
					byte[] data = new byte[instream.available()];
					int nLen = instream.read(data);

					if (nLen < 0) {
						throw new Exception("network error.");
					}

					// 转换输出结果并打印出来
					String temp = new String(data, 0, nLen, "UTF-8");
					if (temp != null && temp.length() > 1) {
						System.out.print(temp);
					}
					Thread.sleep(1000);
				}
//				 outstream.close();
//				 instream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.disconnect();
				channel.disconnect();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
