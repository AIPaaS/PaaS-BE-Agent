package com.ai.platform.agent.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.entity.AgentConfigInfoServer;
import com.ai.platform.agent.server.incoming.AgentServerInitializer;

public class ServerMain {
	public static Logger logger = LogManager.getLogger(ServerMain.class);

	private static ServerMain server = null;

	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	@SuppressWarnings("unused")
	private boolean isStartUp = false;

	private ServerMain() {

	}

	public static ServerMain getInstance() {
		if (server == null) {
			server = new ServerMain();
		}
		return server;
	}

	public void start() {
		AgentConfigInfoServer agentConfigInfoServer = new AgentConfigInfoServer();
		//
		String serverAddr = agentConfigInfoServer.getAgentConfigInfo().getAgentServerIp();//ConfigInit.serverConstant.get(AgentConstant.SERVER_IP);
		int port = Integer.valueOf(agentConfigInfoServer.getAgentConfigInfo().getAgentServerPort());//Integer.valueOf(ConfigInit.serverConstant.get(AgentConstant.SERVER_PORT));
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
					.option(ChannelOption.SO_KEEPALIVE, true).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new AgentServerInitializer());
			ChannelFuture f = b.bind(serverAddr, port).sync();
			isStartUp = true;
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error(e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void destroy() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	// public static void main(String[] args) {
	// String serverAddr =
	// ConfigInit.serverConstant.get(AgentConstant.SERVER_IP);
	// int port =
	// Integer.valueOf(ConfigInit.serverConstant.get(AgentConstant.SERVER_PORT));
	//
	// EventLoopGroup bossGroup = new NioEventLoopGroup();
	// EventLoopGroup workerGroup = new NioEventLoopGroup();
	// try {
	// ServerBootstrap b = new ServerBootstrap();
	// b.group(bossGroup,
	// workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,
	// 100)
	// .option(ChannelOption.SO_KEEPALIVE, true).handler(new
	// LoggingHandler(LogLevel.INFO))
	// .childHandler(new AgentServerInitializer());
	// ChannelFuture f = b.bind(serverAddr, port).sync();
	// // new SessionTest().startThread();
	// new FileTransTest().startThread();
	// f.channel().closeFuture().sync();
	// } catch (InterruptedException e) {
	// logger.error(e);
	// } finally {
	// bossGroup.shutdownGracefully();
	// workerGroup.shutdownGracefully();
	// }
	// }
}
