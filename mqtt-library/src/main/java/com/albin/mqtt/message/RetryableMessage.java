package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public abstract class RetryableMessage extends Message {
	
	private int messageId = -1;

	public RetryableMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}

	public RetryableMessage(Type type) {
		super(type);
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageId() {
		if (messageId == -1) {
			messageId = nextId();
		}
		return messageId;
	}

}
