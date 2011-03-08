package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class RetryableMessage extends Message {
	
	private int messageId;

	public RetryableMessage(Header header) throws IOException {
		super(header);
	}

	public RetryableMessage(Type type) {
		super(type);
	}
	
	@Override
	protected int messageLength() {
		return 2;
	}
	
	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		int id = getMessageId();
		int lsb = id & 0xFF;
		int msb = (id & 0xFF00) >> 8;
		out.write(msb);
		out.write(lsb);
	}

	@Override
	protected void readMessage(InputStream in, int msgLength) throws IOException {
		int msgId = in.read() * 0xFF + in.read();
		setMessageId(msgId);
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
