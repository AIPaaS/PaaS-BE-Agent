package com.ai.platform.agent.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.AgentClientInitializer;
import com.ai.platform.agent.client.outgoing.AuthDataPacket;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.entity.AgentConfigInfoServer;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.ConfigInit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
	static Logger logger = LogManager.getLogger(ClientMain.class);

	public static void main(String[] args) throws AgentServerException {
		AgentConfigInfoServer agentConfigInfoServer = new AgentConfigInfoServer();
		//
		String serverAddr = agentConfigInfoServer.getAgentConfigInfo().getAgentServerIp();//ConfigInit.serverConstant.get(AgentConstant.SERVER_IP);
		int port = Integer.valueOf(agentConfigInfoServer.getAgentConfigInfo().getAgentServerPort());//Integer.valueOf(ConfigInit.serverConstant.get(AgentConstant.SERVER_PORT));
		while (true) {
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true).handler(new AgentClientInitializer());
			try {
				// 发起异步链接操作
				ChannelFuture channelFuture = bootstrap.connect(serverAddr, port);
				channelFuture.sync();
				// 向服务器发送身份验证信息
				AuthDataPacket adp = new AuthDataPacket();
				//
				String agentClientInfo = agentConfigInfoServer.getAgentConfigInfo().getAgentClientInfo();
				//
				byte[] authPacket = adp.genDataPacket(null,agentClientInfo);
				channelFuture.channel().writeAndFlush(authPacket);
				logger.info("agent client [{}] Launch on-line operation , Send authentication information：{}",
						adp.getAuthJson().getString(AgentConstant.CHANNEL_SHOW_KEY), adp.getAuthJson());
				channelFuture.channel().closeFuture().sync();
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("sleep fail,{}", e);
				}
				ShellChannelCollectionUtil.userChannelMap.clear();
				group.shutdownGracefully();
			}
		}
	}
}
