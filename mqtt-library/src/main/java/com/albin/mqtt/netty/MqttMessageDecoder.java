package com.albin.mqtt.netty;

import java.io.ByteArrayInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.albin.mqtt.message.Message;
import com.albin.mqtt.message.MessageInputStream;

public class MqttMessageDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buf) throws Exception {
		if (buf.readableBytes() < 2) {
			return null;
		}
		buf.markReaderIndex();
		buf.readByte(); // read away header
		int msgLength = 0;
		int multiplier = 1;
		int digit;
		int lengthSize = 0;
		do {
			lengthSize++;
			digit = buf.readByte();
			msgLength += (digit & 0x7f) * multiplier;
			multiplier *= 128;
			if ((digit & 0x80) > 0 && !buf.readable()) {
				buf.resetReaderIndex();
				return null;
			}
		} while ((digit & 0x80) > 0);
		if (buf.readableBytes() < msgLength) {
			buf.resetReaderIndex();
			return null;
		}
		byte[] data = new byte[1 + lengthSize + msgLength];
		buf.resetReaderIndex();
		buf.readBytes(data);
		MessageInputStream mis = new MessageInputStream(
				new ByteArrayInputStream(data));
		Message msg = mis.readMessage();
		return msg;
	}

}
