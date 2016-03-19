package com.ai.platform.agent.web.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.entity.Server2ClientMsgMVO;
import com.ai.platform.agent.entity.ShellCommandMVO;
import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.services.ShellCommandSer;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/sshCommand", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
public class AgentManageSSHCommandAPiCtl {

	public static Logger logger = LogManager.getLogger(AgentManageSSHCommandAPiCtl.class);

	@Autowired
	ShellCommandSer commandSer;

	private Server2ClientMsgMVO convertJSON(String jsonStr) {
		Server2ClientMsgMVO sendMsg = JSON.parseObject(jsonStr, Server2ClientMsgMVO.class);
		return sendMsg;
	}

	/***
	 * 开启远端服务器会话
	 * 
	 * @param root
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initSession")
	@ResponseBody
	public String initCommandSession(@RequestBody String jsonStr) {
		Server2ClientMsgMVO sendMsg = convertJSON(jsonStr);
		String result = null;
		try {
			result = commandSer.initCommandSession(sendMsg);
		} catch (Exception e) {
			ResultMessageMVO re = new ResultMessageMVO(ResultCodeConstants.FAIL, e.getMessage());
			result = JSON.toJSONString(re);
			logger.error("操作失败{}", e);
		}
		return result;
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
	public String execCommand(@RequestBody String jsonStr) throws Exception {
		Server2ClientMsgMVO sendMsg = convertJSON(jsonStr);
		String result = null;
		try {
			result = commandSer.execCommand(sendMsg);
		} catch (Exception e) {
			ResultMessageMVO re = new ResultMessageMVO(ResultCodeConstants.FAIL, e.getMessage());
			result = JSON.toJSONString(re);
			logger.error("操作失败{}", e);
		}
		return result;
	}

	/***
	 * 销毁会话
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/destroySession")
	@ResponseBody
	public String destroyCommand(@RequestBody String jsonStr) throws Exception {
		Server2ClientMsgMVO sendMsg = convertJSON(jsonStr);
		String result = null;
		try {
			result = commandSer.destroyCommand(sendMsg);
		} catch (Exception e) {
			ResultMessageMVO re = new ResultMessageMVO(ResultCodeConstants.FAIL, e.getMessage());
			result = JSON.toJSONString(re);
			logger.error("操作失败{}", e);
		}
		return result;
	}

	public static void main(String[] args) {
		ShellCommandMVO command = new ShellCommandMVO();
		HostInfo host = new HostInfo();
		command.setHostInfo(host);

		host.setHost("10.1.235.197");
		host.setUserName("swuser01");
		host.setPassword("swuser01@123");
		host.setPort(22);

		UUID id = UuidUtil.getTimeBasedUuid();
		String uid = id.randomUUID().toString().replaceAll("-", "");
		String sid = id.randomUUID().toString().replaceAll("-", "");

		String aid = "A";

		command.setAid(aid);
		command.setUid(uid);
		command.setSid(sid);

		command.setCommand("ls -al \n");

		command.setCallBackType("url");
		command.setCallBackContent("http://www.baidu.com");

		JSONObject bussStr = new JSONObject();
		bussStr.put("sid", sid);
		bussStr.put("测试", "业务信息");

		Server2ClientMsgMVO sendMsg = new Server2ClientMsgMVO();
		sendMsg.setBussInfo(bussStr.toJSONString());
		sendMsg.setCommandInfo(command);

		System.out.println(JSON.toJSONString(sendMsg));

	}
}
