package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public class PubRecMessage extends RetryableMessage {

	public PubRecMessage(int messageId) {
		super(Type.PUBREC);
		setMessageId(messageId);
	}

	public PubRecMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}
}
