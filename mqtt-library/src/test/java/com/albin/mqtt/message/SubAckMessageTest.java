package com.albin.mqtt.message;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.albin.mqtt.message.Message.Header;


public class SubAckMessageTest {
	
	@Test
	public void isDeserializedCorrectly() throws IOException {
		Header header = new Header((byte) 0x90);
		InputStream in = new ByteArrayInputStream(new byte[] {5, 0, 5, 2, 0, 1});
		SubAckMessage msg = new SubAckMessage(header);
		msg.read(in);
		assertEquals(5, msg.getMessageId());
		List<QoS> qoses = msg.getGrantedQoSs();
		assertEquals(3, qoses.size());
		assertEquals(QoS.EXACTLY_ONCE, qoses.get(0));
		assertEquals(QoS.AT_MOST_ONCE, qoses.get(1));
		assertEquals(QoS.AT_LEAST_ONCE, qoses.get(2));
	}

}
