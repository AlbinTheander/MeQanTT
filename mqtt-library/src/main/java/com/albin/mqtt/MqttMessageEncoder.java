package com.albin.mqtt;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.albin.mqtt.message.Message;

public class MqttMessageEncoder extends OneToOneEncoder implements
		ChannelHandler {

	@Override
	protected Object encode(ChannelHandlerContext ctc, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof Message)) {
			return null;
		}
		byte[] data = ((Message) msg).toBytes();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(data); // data
		return buf;
	}

}
