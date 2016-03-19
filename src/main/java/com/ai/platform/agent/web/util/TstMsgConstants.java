package com.ai.platform.agent.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TstMsgConstants {
	public final static Map<String, List<String>> msgMap = new HashMap<String, List<String>>();

	public static void put(String key, String msg) {
		List<String> msgList = msgMap.get(key);
		msgList.add(msg);
	}

	public static void remove(String key) {
		msgMap.remove(key);
	}
}
