package com.ai.platform.agent.client.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.jcraft.jsch.JSch;

public class UserSftpChannelInfo extends UserChannelInfo {

	public static Logger logger = LogManager.getLogger(UserSftpChannelInfo.class);
	
	private HostFileInfo fileInfo ;
	
	@Override
	public void firstSSHAction(HostInfo hostInfo) {
		try {
			JSch jsch = new JSch();
			super.session = jsch.getSession(hostInfo.getUserName(), hostInfo.getHost(), hostInfo.getPort());
			super.session.setPassword(hostInfo.getPassword());
			super.session.setConfig("StrictHostKeyChecking", "no");
			super.session.connect(30000);

			super.channel = super.session.openChannel("sftp");
			super.channel.connect();

		} catch (Exception e) {
			logger.error("创建localSFTP会话异常");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void checkChannelIsOpen() throws AgentServerException {
		if (super.session == null) {
			throw new AgentServerException("当前session未创建,sid:{" + userSessionInfo.getSid() + "}");
		}
		if (!super.session.isConnected()) {
			throw new AgentServerException("当前session未连接,sid:{" + userSessionInfo.getSid() + "}");
		}
		if (super.channel == null) {
			throw new AgentServerException("当前channel未创建,sid:{" + userSessionInfo.getSid() + "}");
		}
		if (super.channel.isClosed()) {
			throw new AgentServerException("当前channel未连接,sid:{" + userSessionInfo.getSid() + "}");
		}
	}

	public HostFileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(HostFileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}
}
