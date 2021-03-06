package com.ai.platform.agent.client.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClientTest {
	private static Logger log = LogManager.getLogger(HttpClientTest.class);
	/**
	 * @author zhangzhongde
	 * @date 2013-03-19
	 */
	@Test
	public void simpFileUpload(){
		//
		//String url = "http://10.1.245.249:29806/agent-web-api/simpFile/upload";
		//String url = "http://10.1.241.125:16204/platform.agent.web/simpFile/upload";
		String url = "http://10.1.241.125:16204/agent-web-api/simpFile/upload";
		//simpFile/upload
		UploadBean uploadBean = new UploadBean();
		uploadBean.setAid("A");
		uploadBean.setContent("echo 12343243243233333333fdsafsafsafsafs3333333333333333333");
		uploadBean.setFileName("zhangzhongde.log");
		uploadBean.setPath("/root");
		//
		JSONObject jsonObject = HttpXmlClient.doPost(url, JSONObject.parseObject(JSON.toJSONString(uploadBean)));
		log.info("-----info-----:"+jsonObject.toJSONString());
	}
	@Test
	public void simpCommandExec(){
		//
		//String url = "http://10.1.245.249:29806/agent-web-api/simpCommand/exec";
		//String url = "http://10.1.241.125:16204/platform.agent.web/simpCommand/exec";
		String url = "http://10.1.241.125:16204/agent-web-api/simpCommand/exec";
		//simpFile/upload
		CommandBean commandBean = new CommandBean();
		commandBean.setAid("A");
		commandBean.setCommand("chmod u+x /root/zhangzhongde.log");
		//
		JSONObject jsonObject = HttpXmlClient.doPost(url, JSONObject.parseObject(JSON.toJSONString(commandBean)));
		log.info("-----info-----:"+jsonObject.toJSONString());
	}
	

}
