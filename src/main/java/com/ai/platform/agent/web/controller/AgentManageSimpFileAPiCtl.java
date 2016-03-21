package com.ai.platform.agent.web.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.platform.agent.entity.SimpleFileReqInfo;
import com.ai.platform.agent.entity.SimpleFileResInfo;
import com.ai.platform.agent.web.services.SimpFileCommandSer;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "/simpFile", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
public class AgentManageSimpFileAPiCtl {

	public static Logger logger = LogManager.getLogger(AgentManageSimpFileAPiCtl.class);

	@Autowired
	SimpFileCommandSer fileCommandSer;

	private SimpleFileReqInfo convertJSON(String jsonStr) {
		SimpleFileReqInfo sendMsg = JSON.parseObject(jsonStr, SimpleFileReqInfo.class);
		return sendMsg;
	}

	/***
	 * 开启远端服务器会话
	 * 
	 * @param root
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload")
	@ResponseBody
	public String initCommandSession(@RequestBody String jsonStr) {
		SimpleFileReqInfo sendMsg = convertJSON(jsonStr);
		
		String key = sendMsg.getKey();
		if(Strings.isBlank(key)){
			key = UUID.randomUUID().toString();
			sendMsg.setKey(key);
		}
		
		String result = null;
		try {
			result = fileCommandSer.uploadFile(sendMsg);
		} catch (Exception e) {
			SimpleFileResInfo re = new SimpleFileResInfo(ResultCodeConstants.FAIL, e.getMessage());
			result = JSON.toJSONString(re);
			logger.error("Operation failed {}", e);
		}
		return result;
	}
	
	public static void main(String[] args){
		SimpleFileReqInfo req = new SimpleFileReqInfo();
		req.setKey("12345");
		req.setAid("A");
		req.setContent("echo '12321'");
		req.setPath("~");
		req.setFileName("test.sh");
		System.out.println(JSON.toJSONString(req));
	}

}
