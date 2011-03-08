package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public class UnsubAckMessage extends RetryableMessage {

	public UnsubAckMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}

}
