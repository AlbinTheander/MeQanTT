package com.albin.mqtt.message;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class PingReqMessageTest {

	private PingReqMessage msg;

	@Before
	public void setUp() {
		msg = new PingReqMessage();
	}

	@Test
	public void messageIsCorrectlyEncoded() {
		byte[] data = msg.toBytes();
		assertEquals(0xC0, data[0] & 0xF0); // messageId = 12
		assertEquals(0, data[1]); // remaining length = 0;
	}

	@Test(expected = UnsupportedOperationException.class)
	public void dupFlagIsNotAllowed() {
		msg.setDup(true);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void qosFlagsAreNotAllowed() {
		msg.setQos(QoS.AT_LEAST_ONCE);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void retainFlagIsNotAllowed() {
		msg.setRetained(true);
	}
}
