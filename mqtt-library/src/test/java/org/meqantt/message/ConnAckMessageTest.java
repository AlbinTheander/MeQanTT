/*******************************************************************************
 * Copyright 2011 Albin Theander
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.meqantt.message;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.meqantt.message.ConnAckMessage;
import org.meqantt.message.ConnAckMessage.ConnectionStatus;


public class ConnAckMessageTest {

	@Test(expected = IllegalStateException.class)
	public void wrongLengthDataThrowsException() throws IOException {
		byte[] data = new byte[] { 0, 1, 2 };
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ConnAckMessage msg = new ConnAckMessage();
		msg.readMessage(bais, 3);
	}

	@Test(expected = UnsupportedOperationException.class)
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

	@Test
	public void correctMeessageSize() throws IOException {
		ConnAckMessage msg = new ConnAckMessage(ConnectionStatus.ACCEPTED);
		byte[] data = msg.toBytes();
		// header(1) + messageSize(1) + 2 = 4
		assertEquals(4, data.length);
		assertEquals(2, msg.messageLength());
	}

	@Test
	public void correctStatus() throws IOException {
		ConnAckMessage msg = new ConnAckMessage(ConnectionStatus.ACCEPTED);
		byte[] data = msg.toBytes();
		assertEquals(0x00, data[3]);

		msg = new ConnAckMessage(ConnectionStatus.UNACCEPTABLE_PROTOCOL_VERSION);
		data = msg.toBytes();
		assertEquals(0x01, data[3]);

		msg = new ConnAckMessage(ConnectionStatus.IDENTIFIER_REJECTED);
		data = msg.toBytes();
		assertEquals(0x02, data[3]);

		msg = new ConnAckMessage(ConnectionStatus.SERVER_UNAVAILABLE);
		data = msg.toBytes();
		assertEquals(0x03, data[3]);

		msg = new ConnAckMessage(ConnectionStatus.BAD_USERNAME_OR_PASSWORD);
		data = msg.toBytes();
		assertEquals(0x04, data[3]);

		msg = new ConnAckMessage(ConnectionStatus.NOT_AUTHORIZED);
		data = msg.toBytes();
		assertEquals(0x05, data[3]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void statusCanNotBeNull() {
		new ConnAckMessage((ConnectionStatus) null);
	}

	private void assertParse(int code, ConnectionStatus expected)
			throws IOException {
		byte[] data = new byte[] { 0, (byte) code };
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ConnAckMessage msg = new ConnAckMessage();
		msg.readMessage(bais, 2);
		assertEquals(expected, msg.getStatus());
	}

}
