package com.ai.platform.agent.entity;

public class AgentSftpInfo {

	private HostFileInfo srcFileSftpInfo;

	private HostFileInfo descFileSftpInfo;
	
	private String aid;

	private String uid;

	private String sid;
	
	private String fid;
	
	private String callBackType;

	private String callBackContent;

	public HostFileInfo getSrcFileSftpInfo() {
		return srcFileSftpInfo;
	}

	public void setSrcFileSftpInfo(HostFileInfo srcFileSftpInfo) {
		this.srcFileSftpInfo = srcFileSftpInfo;
	}

	public HostFileInfo getDescFileSftpInfo() {
		return descFileSftpInfo;
	}

	public void setDescFileSftpInfo(HostFileInfo descFileSftpInfo) {
		this.descFileSftpInfo = descFileSftpInfo;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
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

	public String getCallBackType() {
		return callBackType;
	}

	public void setCallBackType(String callBackType) {
		this.callBackType = callBackType;
	}

	public String getCallBackContent() {
		return callBackContent;
	}

	public void setCallBackContent(String callBackContent) {
		this.callBackContent = callBackContent;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	@Override
	public String toString() {
		return "AgentSftpInfo [srcFileSftpInfo=" + srcFileSftpInfo + ", descFileSftpInfo=" + descFileSftpInfo + ", aid="
				+ aid + ", uid=" + uid + ", sid=" + sid + ", fid=" + fid + ", callBackType=" + callBackType
				+ ", callBackContent=" + callBackContent + "]";
	}

}
