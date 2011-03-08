package com.albin.mqtt.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.albin.mqtt.message.Message.Header;


public class PingRespMessageTest {
	
	@Test
	public void isDeserializedCorrectly() throws IOException {
		Header header = new Header((byte) 0xD0);
		InputStream in = new ByteArrayInputStream(new byte[] {0});
		PingRespMessage msg = new PingRespMessage(header);
		msg.read(in);
	}

}
