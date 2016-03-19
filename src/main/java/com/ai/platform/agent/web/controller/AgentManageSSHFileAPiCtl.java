package com.ai.platform.agent.web.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.platform.agent.entity.AgentSftpInfo;
import com.ai.platform.agent.entity.HostFileInfo;
import com.ai.platform.agent.entity.HostInfo;
import com.ai.platform.agent.entity.Server2ClientFileMsgMVO;
import com.ai.platform.agent.entity.ShellCommandMVO;
import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.services.ShellFileCommandSer;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/file")
public class AgentManageSSHFileAPiCtl {

	public static Logger logger = LogManager.getLogger(AgentManageSSHFileAPiCtl.class);

	@Autowired
	ShellFileCommandSer commandSer;

	private Server2ClientFileMsgMVO convertJSON(String jsonStr) {
		Server2ClientFileMsgMVO sendMsg = JSON.parseObject(jsonStr, Server2ClientFileMsgMVO.class);
		return sendMsg;
	}

	/***
	 * 对于文件暂时只支持单个文件一个会话
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadFile", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String execCommand(@RequestBody String jsonStr) throws Exception {
		Server2ClientFileMsgMVO sendMsg = convertJSON(jsonStr);
		String result = null;
		try {
			result = commandSer.uploadFile(sendMsg);
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
		String fid = id.randomUUID().toString().replaceAll("-", "");

		String aid = "A";
		AgentSftpInfo agentSftpInfo = new AgentSftpInfo();
		
		HostFileInfo srcFileSftpInfo = new HostFileInfo();
		srcFileSftpInfo.setHost("10.1.235.197");
		srcFileSftpInfo.setUserName("swuser01");
		srcFileSftpInfo.setPassword("swuser01@123");
		srcFileSftpInfo.setPath("/aifs01/users/swuser01");
		srcFileSftpInfo.setFileName("d.txt");

		HostFileInfo descFileSftpInfo = new HostFileInfo();
		descFileSftpInfo.setHost("10.1.235.197");
		descFileSftpInfo.setUserName("swuser01");
		descFileSftpInfo.setPassword("swuser01@123");
		descFileSftpInfo.setPath("/aifs01/users/swuser01/test_dir");
		descFileSftpInfo.setFileName("dddd.txt");
		
		agentSftpInfo.setDescFileSftpInfo(descFileSftpInfo);
		agentSftpInfo.setSrcFileSftpInfo(srcFileSftpInfo);
		agentSftpInfo.setAid(aid);
		agentSftpInfo.setSid(sid);
		agentSftpInfo.setUid(uid);
		agentSftpInfo.setFid(fid);
		agentSftpInfo.setCallBackType("url");
		agentSftpInfo.setCallBackContent("www.baidu.com");

		JSONObject bussStr = new JSONObject();
		bussStr.put("sid", sid);
		bussStr.put("测试", "业务信息");

		Server2ClientFileMsgMVO sendMsg = new Server2ClientFileMsgMVO();
		sendMsg.setBussInfo(bussStr.toJSONString());
		sendMsg.setTransFileInfo(agentSftpInfo);

		System.out.println(JSON.toJSONString(sendMsg));

	}
}
