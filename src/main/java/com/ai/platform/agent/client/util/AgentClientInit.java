package com.ai.platform.agent.client.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.entity.AgentClientInfo;
import com.ai.platform.agent.entity.OSServerInfo;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.ConfigInit;
import com.alibaba.fastjson.JSONObject;

public class AgentClientInit {

	public static Logger logger = LogManager.getLogger(AgentClientInit.class);

	public static Map<String, String> clientConstant = new HashMap<String, String>();

	static {
		Properties p = new Properties();
		String fileName = AgentConstant.AGENT_CLIENT_FILE_PATH+AgentConstant.AGENT_CLIENT_CONFIG_FILE_NAME;
		File file = new File(fileName);
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					ConfigInit.class.getClassLoader().getResourceAsStream(AgentConstant.AGENT_CLIENT_CONFIG_FILE_NAME),
					"UTF-8"));
			//BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8")); 
			p.load(bf);
			for (Entry<Object, Object> tmpEntry : p.entrySet()) {
				clientConstant.put((String) tmpEntry.getKey(), (String) tmpEntry.getValue());
				logger.info("初始化key[{}]的值为{{}}", (String) tmpEntry.getKey(), (String) tmpEntry.getValue());
			}
		} catch (IOException e) {
			logger.error("{}配置文件读取异常");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(AgentClientInit.clientConstant);
	}
}
