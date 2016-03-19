package com.ai.platform.agent.client.entity;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.exception.MessageParserException;
import com.ai.platform.agent.util.AgentConstant;

/***
 * [32 uid][32 sid][32 fid][message] [文件信息json]文件开始的内容包 [文件内容]文件接收的内容包
 * [关闭文件]文件结束的内容－－其实是结束指令，内容基本不用
 * 
 * @author Administrator
 *
 */
public class FilePacket {

	public static Logger logger = LogManager.getLogger(FilePacket.class);

	private String uid;
	private String sid;
	private String fid;
	private String message;

	private byte[] dataByteArray;

	public void init(byte[] msg) throws MessageParserException {
		// 继续分解包体
		byte[] uidByteArray = new byte[AgentConstant.UID_LENGTH];
		byte[] sidByteArray = new byte[AgentConstant.SID_LENGTH];
		byte[] fidByteArray = new byte[AgentConstant.FID_LENGTH];
		dataByteArray = new byte[msg.length - AgentConstant.UID_LENGTH - AgentConstant.SID_LENGTH
				- AgentConstant.FID_LENGTH];

		System.arraycopy(msg, 0, uidByteArray, 0, AgentConstant.UID_LENGTH);
		System.arraycopy(msg, AgentConstant.UID_LENGTH, sidByteArray, 0, AgentConstant.SID_LENGTH);
		System.arraycopy(msg, AgentConstant.UID_LENGTH + AgentConstant.SID_LENGTH, fidByteArray, 0,
				AgentConstant.FID_LENGTH);
		System.arraycopy(msg, AgentConstant.UID_LENGTH + AgentConstant.SID_LENGTH + AgentConstant.FID_LENGTH, dataByteArray, 0,
				msg.length - AgentConstant.UID_LENGTH - AgentConstant.SID_LENGTH - AgentConstant.FID_LENGTH);

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
			fid = new String(fidByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("fid 转义异常, fid内容：{}", fidByteArray);
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

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public byte[] getDataByteArray() {
		return dataByteArray;
	}

	public void setDataByteArray(byte[] dataByteArray) {
		this.dataByteArray = dataByteArray;
	}

}
