package com.albin.mqtt.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.albin.mqtt.message.Message.Header;

public class MessageInputStream implements Closeable{
	
	private InputStream in;
	
	public MessageInputStream(InputStream in) {
		this.in = in;
	}

	public Message readMessage() throws IOException {
		byte flags = (byte) in.read();
		Header header = new Header(flags);
		switch (header.getType()) {
		case CONNECT:
			throw new UnsupportedOperationException("No support for deserializing CONNECT messages");
		case CONNACK:
			return new ConnAckMessage(header, in);
		case PUBLISH:
			return new PublishMessage(header, in);
		case SUBACK:
			return new SubAckMessage(header, in);
		default:
			break;
		}
		return null;
	}

	public void close() throws IOException {
		in.close();
	}
}
