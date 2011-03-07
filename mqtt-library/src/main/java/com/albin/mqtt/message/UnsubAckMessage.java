package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public class UnsubAckMessage extends RetryableMessage {

	public UnsubAckMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}
	
	@Override
	protected void readMessage(InputStream in, int msgLength)
			throws IOException {
		int msgId = in.read() * 0xFF + in.read();
		setMessageId(msgId);
	}

}
