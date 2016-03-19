package com.ai.platform.agent.client.incoming;

import com.ai.platform.agent.client.incoming.handler.IncomingMessageHandler;
import com.ai.platform.agent.client.outgoing.handler.OutgoingMessageHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class AgentClientInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("decoder", new ByteArrayDecoder());
		p.addLast("encoder", new ByteArrayEncoder());
		p.addLast(new IncomingMessageHandler());
		p.addLast(new OutgoingMessageHandler());
	}

}
