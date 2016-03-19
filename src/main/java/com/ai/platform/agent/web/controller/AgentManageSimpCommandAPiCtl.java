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

import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.services.SimpCommandSer;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "/simpCommand", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
public class AgentManageSimpCommandAPiCtl {

	public static Logger logger = LogManager.getLogger(AgentManageSimpCommandAPiCtl.class);

	@Autowired
	SimpCommandSer commandSer;

	private SimpleCommandReqInfo convertJSON(String jsonStr) {
		SimpleCommandReqInfo commadInfo = new SimpleCommandReqInfo();
		logger.info("jsonStr:"+jsonStr);
		try{
			commadInfo = JSON.parseObject(jsonStr, SimpleCommandReqInfo.class);
		}catch(Exception e){
			logger.info(e.getMessage());
			
		}
		return commadInfo;
		
	}

	/***
	 * 执行命令
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exec")
	@ResponseBody
	public String execCommand( String jsonStr) throws Exception {
		SimpleCommandReqInfo commadInfo = convertJSON(jsonStr);
		
		String key = commadInfo.getKey();
		if(Strings.isBlank(key)){
			key = UUID.randomUUID().toString();
			commadInfo.setKey(key);
		}
		
		String result = null;
		try {
			result = commandSer.execCommand(commadInfo);
		} catch (Exception e) {
			ResultMessageMVO re = new ResultMessageMVO(ResultCodeConstants.FAIL, e.getMessage());
			result = JSON.toJSONString(re);
			logger.error("operating {}", e);
		}
		return result;
	}


	public static void main(String[] args) {

	}
}
