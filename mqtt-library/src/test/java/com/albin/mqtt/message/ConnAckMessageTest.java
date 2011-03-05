package com.albin.mqtt.message;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.albin.mqtt.message.ConnAckMessage.ConnectionStatus;


public class ConnAckMessageTest {
	
	
	@Test(expected=IllegalStateException.class)
	public void wrongLengthDataThrowsException() throws IOException {
		byte[] data = new byte[]{0, 1, 2};
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ConnAckMessage msg = new ConnAckMessage();
		msg.readMessage(bais, 3);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void unsupportedStatusCodeThrowsException() throws IOException {
		assertParse(7, ConnectionStatus.ACCEPTED);
	}
	
	@Test
	public void correctStatusCodesAreParsed() throws IOException {
		assertParse(0, ConnectionStatus.ACCEPTED);
		assertParse(1, ConnectionStatus.UNACCEPTABLE_PROTOCOL_VERSION);
		assertParse(2, ConnectionStatus.IDENTIFIER_REJECTED);
		assertParse(3, ConnectionStatus.SERVER_UNAVAILABLE);
		assertParse(4, ConnectionStatus.BAD_USERNAME_OR_PASSWORD);
		assertParse(5, ConnectionStatus.NOT_AUTHORIZED);
	}

	private void assertParse(int code, ConnectionStatus expected) throws IOException {
		byte[] data = new byte[]{0, (byte) code};
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ConnAckMessage msg = new ConnAckMessage();
		msg.readMessage(bais, 2);
		assertEquals(expected, msg.getStatus());
	}

}
