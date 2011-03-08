package com.albin.mqtt.message;

import java.io.IOException;

public class PingRespMessage extends Message {

	public PingRespMessage(Header header) throws IOException {
		super(header);
	}
}
