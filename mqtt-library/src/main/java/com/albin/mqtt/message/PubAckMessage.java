package com.albin.mqtt.message;

import java.io.IOException;

public class PubAckMessage extends RetryableMessage {

	public PubAckMessage(int messageId) {
		super(Type.PUBACK);
		setMessageId(messageId);
	}

	public PubAckMessage(Header header) throws IOException {
		super(header);
	}
}
