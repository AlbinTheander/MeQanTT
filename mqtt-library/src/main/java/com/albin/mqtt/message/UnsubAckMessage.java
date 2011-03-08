package com.albin.mqtt.message;

import java.io.IOException;

public class UnsubAckMessage extends RetryableMessage {

	public UnsubAckMessage(Header header) throws IOException {
		super(header);
	}

}
