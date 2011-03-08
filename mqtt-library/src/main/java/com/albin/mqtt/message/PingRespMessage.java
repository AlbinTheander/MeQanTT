package com.albin.mqtt.message;

import java.io.IOException;
import java.io.InputStream;

public class PingRespMessage extends Message {

	public PingRespMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}
}
