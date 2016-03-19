package com.ai.platform.agent.server.incoming;

import com.ai.platform.agent.server.incoming.handler.ChannelManageHandler;
import com.ai.platform.agent.server.incoming.handler.IncomingMessageHandler;
import com.ai.platform.agent.server.outgoing.OutgoingMessageHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class AgentServerInitializer extends ChannelInitializer<io.netty.channel.socket.SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("decoder", new ByteArrayDecoder());
		p.addLast("encoder", new ByteArrayEncoder());
		p.addLast(new ChannelManageHandler());
		p.addLast(new IncomingMessageHandler());
		p.addLast(new OutgoingMessageHandler());
	}
}
