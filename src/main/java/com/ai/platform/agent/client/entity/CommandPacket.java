package com.ai.platform.agent.client.entity;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.util.AgentConstant;

public class CommandPacket {

	public static Logger logger = LogManager.getLogger(CommandPacket.class);

	private String uid;
	private String sid;
	private String message;

	public void init(byte[] msg) throws MessageParserException {
		// 继续分解包体
		byte[] uidByteArray = new byte[AgentConstant.UID_LENGTH];
		byte[] sidByteArray = new byte[AgentConstant.SID_LENGTH];
		byte[] dataByteArray = new byte[msg.length - AgentConstant.UID_LENGTH - AgentConstant.SID_LENGTH];

		System.arraycopy(msg, 0, uidByteArray, 0, AgentConstant.UID_LENGTH);
		System.arraycopy(msg, AgentConstant.UID_LENGTH, sidByteArray, 0, AgentConstant.SID_LENGTH);
		System.arraycopy(msg, AgentConstant.UID_LENGTH + AgentConstant.SID_LENGTH, dataByteArray, 0, msg.length - AgentConstant.UID_LENGTH - AgentConstant.SID_LENGTH);

		try {
			uid = new String(uidByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("uid 转义异常, uid内容：{}", uidByteArray);
		}
		try {
			sid = new String(sidByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("sid 转义异常, sid内容：{}", sidByteArray);
		}
		try {
			message = new String(dataByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("消息体 转义异常，data内容：{}", dataByteArray);
			throw new MessageParserException("parse byte[] to string failure.", e);
		}
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
