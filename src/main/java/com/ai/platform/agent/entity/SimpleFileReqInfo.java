package com.ai.platform.agent.entity;

public class SimpleFileReqInfo {
	
	private String key;

	private String aid;

	private String content;
	
	private String path;
	
	private String fileName;

	private String code;
	
	private String msg;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SimpleFileReqInfo [key=" + key + ", aid=" + aid + ", content=" + content + ", path=" + path
				+ ", fileName=" + fileName + ", code=" + code + ", msg=" + msg + "]";
	}

}
