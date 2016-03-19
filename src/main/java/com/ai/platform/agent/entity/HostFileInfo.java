package com.ai.platform.agent.entity;

public class HostFileInfo extends HostInfo {
	private String fileName;

	private String path;

	private long fileLength = 0l;

	private String md5Str;

	private long recFileLength = 0l;

	public long addFileLength(long fileLength) {
		recFileLength += fileLength;
		return this.recFileLength;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getMdsStr() {
		return md5Str;
	}

	public void setMdsStr(String mdsStr) {
		this.md5Str = mdsStr;
	}

	@Override
	public String toString() {
		return "HostFileInfo [fileName=" + fileName + ", path=" + path + ", fileLength=" + fileLength + ", md5Str="
				+ md5Str + ", recFileLength=" + recFileLength + "]";
	}

}
