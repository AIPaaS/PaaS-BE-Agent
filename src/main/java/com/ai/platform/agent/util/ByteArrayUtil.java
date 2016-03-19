package com.ai.platform.agent.util;

public class ByteArrayUtil {

	/***
	 * 数据封包通用方法
	 * 
	 * @param args
	 * @return
	 */
	public static byte[] mergeByteArray(byte[]... args) {
		int byteSize = 0;
		for (int i = 0; i < args.length; i++) {
			byteSize += args[i].length;
		}

		int copyStart = 0;
		byte[] authPacket = new byte[byteSize];
		for (int i = 0; i < args.length; i++) {
			int curBytelength = args[i].length;
			System.arraycopy(args[i], 0, authPacket, copyStart, curBytelength);
			copyStart += curBytelength;
		}
		return authPacket;
	}
}
