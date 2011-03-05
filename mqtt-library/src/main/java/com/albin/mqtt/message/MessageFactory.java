package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

import com.albin.mqtt.message.Message.Header;

public class MessageFactory {

	public Message read(InputStream in) throws IOException {
		byte flags = (byte) in.read();
		Header header = new Header(flags);
		switch (header.getType()) {
		case CONNECT:
			throw new UnsupportedOperationException("No support for deserializing CONNECT messages");
		case CONNACK:
			return new ConnAckMessage(header, in);
		default:
			break;
		}
		return null;
	}
}
