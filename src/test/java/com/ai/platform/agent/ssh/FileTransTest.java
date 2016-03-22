package com.ai.platform.agent.ssh;

import java.io.File;

public class FileTransTest {

	public static void main(String[] args) {
		File f = new File("E:\\a.txt");
		if (f.exists() && f.isFile()) {
			System.out.println("" + f.length());
		} else {
			System.out.println("file doesn't exist or is not a file");
		}
	}

}
