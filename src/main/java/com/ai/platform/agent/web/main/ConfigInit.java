package com.ai.platform.agent.web.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigInit {

	public static Logger logger = LogManager.getLogger(ConfigInit.class);

	public static Map<String, String> serverConstant = new HashMap<String, String>();

	static {
		Properties p = new Properties();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					ConfigInit.class.getClassLoader().getResourceAsStream(JettyConstant.JETTY_CONFIG_PROPERTY_FILE_NAME),
					"UTF-8"));
			p.load(bf);
			String keyStr = null;
			for (Entry<Object, Object> tmpEntry : p.entrySet()) {
				if(((String)tmpEntry.getKey()).startsWith("jetty")){
					keyStr = ((String)tmpEntry.getKey()).substring("jetty.".length());
				}
				serverConstant.put(keyStr, (String) tmpEntry.getValue());
				logger.info("初始化key[{}]的值为{{}}", (String) keyStr, (String) tmpEntry.getValue());
			}
		} catch (IOException e) {
			logger.error("{}配置文件读取异常");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(ConfigInit.serverConstant);
	}
}
