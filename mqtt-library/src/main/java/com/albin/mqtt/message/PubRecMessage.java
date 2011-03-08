package com.albin.mqtt.message;

import java.io.IOException;

public class PubRecMessage extends RetryableMessage {

	public PubRecMessage(int messageId) {
		super(Type.PUBREC);
		setMessageId(messageId);
	}

	public PubRecMessage(Header header) throws IOException {
		super(header);
	}
}
