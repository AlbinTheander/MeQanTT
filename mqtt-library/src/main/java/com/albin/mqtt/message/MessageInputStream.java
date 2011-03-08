package com.albin.mqtt.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.albin.mqtt.message.Message.Header;

public class MessageInputStream implements Closeable {

	private InputStream in;

	public MessageInputStream(InputStream in) {
		this.in = in;
	}

	public Message readMessage() throws IOException {
		byte flags = (byte) in.read();
		Header header = new Header(flags);
		switch (header.getType()) {
		case CONNACK:
			return new ConnAckMessage(header, in);
		case PUBLISH:
			return new PublishMessage(header, in);
		case SUBACK:
			return new SubAckMessage(header, in);
		case UNSUBACK:
			return new UnsubAckMessage(header, in);
		case PINGRESP:
			return new PingRespMessage(header, in);
		default:
			throw new UnsupportedOperationException(
					"No support for deserializing " + header.getType()
							+ " messages");
		}
	}

	public void close() throws IOException {
		in.close();
	}
}
