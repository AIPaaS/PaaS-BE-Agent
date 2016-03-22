package com.ai.platform.agent.ssh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the sftp protocol support.
 *   $ CLASSPATH=.:../build javac Sftp.java
 *   $ CLASSPATH=.:../build java Sftp
 * You will be asked username, host and passwd. 
 * If everything works fine, you will get a prompt 'sftp>'. 
 * 'help' command will show available command.
 * In current implementation, the destination path for 'get' and 'put'
 * commands must be a file, not a directory.
 *
 */
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpNoAsk {
	public static void main(String[] arg) {

		try {
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
			sftp.cd("/aifs01/users/zkpusr01");
			InputStream instream = sftp.get("zookeeper.001.tgz");
			OutputStream outstream = new FileOutputStream(new File("E://zookeeper.001.tgz"));

			byte b[] = new byte[1024 * 1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}
			outstream.flush();
			outstream.close();
			instream.close();

			long endTime = System.currentTimeMillis();

			System.out.println("use " + (endTime - startTime));

			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}