package com.ai.platform.agent.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloAgentApiCtl {

	private static Logger logger = LogManager.getLogger(HelloAgentApiCtl.class);

	@RequestMapping(value = "/help", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String showDefaultIndexPage(ModelMap root) throws Exception {
		return "index";
	}

}
