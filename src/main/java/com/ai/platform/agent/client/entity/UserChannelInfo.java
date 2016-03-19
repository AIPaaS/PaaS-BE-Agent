package com.ai.platform.agent.client.entity;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.entity.UserSessionInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public class UserChannelInfo {

	public static Logger logger = LogManager.getLogger(UserChannelInfo.class);

	/** 记录用户会话信息 */
	UserSessionInfo userSessionInfo = null;
	/** ssh会话 */
	Session session = null;
	/** ssh通道 */
	Channel channel = null;

	InputStream instream = null;

	OutputStream outstream = null;

	public void firstSSHAction(HostInfo sshInfo) {

	}

	public void checkChannelIsOpen() throws AgentServerException {

	}

	public UserSessionInfo getUserSessionInfo() {
		return userSessionInfo;
	}

	public void setUserSessionInfo(UserSessionInfo userSessionInfo) {
		this.userSessionInfo = userSessionInfo;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public InputStream getInstream() {
		return instream;
	}

	public void setInstream(InputStream instream) {
		this.instream = instream;
	}

	public OutputStream getOutstream() {
		return outstream;
	}

	public void setOutstream(OutputStream outstream) {
		this.outstream = outstream;
	}
}
