package com.ai.platform.agent.ssh;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ExecNew {
	public static void main(String[] arg) {
		try {
			JSch jsch = new JSch();

			String host = "10.1.235.19";
			String user = "swuser01";
			final Session session = jsch.getSession(user, host, 22);

			String passwd = "swuser01@123";
			session.setPassword(passwd);

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			session.setTimeout(5000);

			String command = "ls -al \n pwd \n";

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// X Forwarding
			// channel.setXForwarding(true);

			// channel.setInputStream(System.in);
			channel.setInputStream(null);

			// channel.setOutputStream(System.out);

			// FileOutputStream fos=new FileOutputStream("/tmp/stderr");
			// ((ChannelExec)channel).setErrStream(fos);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
					System.out.println("timeOut:"+channel.getSession().getTimeout());
				} catch (Exception ee) {
				}
			}
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}