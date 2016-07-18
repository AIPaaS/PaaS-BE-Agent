package com.ai.platform.agent.entity;

import java.util.ArrayList;
import java.util.List;

public class AgentSSHInfo extends HostInfo{
	
	private List<String> args = null;

	@SuppressWarnings("unused")
	private void addArgs(String args) {
		if (this.args == null) {
			this.args = new ArrayList<String>();
		}
		this.args.add(args);
	}

	@SuppressWarnings("unused")
	private void removeArgs(String args) {
		if (this.args != null) {
			this.args.remove(args);
		}
	}
	
}
