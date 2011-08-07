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
package com.albin.mqtt.message;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class ConnectMessageTest {

	private static final int CLEAN_SESSION_MASK = 0x02;
	private static final int WILL_MASK = 0x04;
	private static final int WILL_QoS_MASK = 0x18;
	private static final int WILL_RETAIN_MASK = 0x20;
	private static final int PASSWORD_MASK = 0x40;
	private static final int USERNAME_MASK = 0x80;

	@Test
	public void clientIdIsMax23Characters() {
		// This should work
		new ConnectMessage("12345678901234567890123", false, 10);
		try {
			// This should not work;
			new ConnectMessage("123456789012345678901234", false, 10);
			fail();
		} catch (IllegalArgumentException e) {

		}
	}

	@Test
	public void messageSizeIsCorrect() {
		ConnectMessage msg = new ConnectMessage("Test", false, 20);
		// Size: Header + "Test" (Strings have an additional 2 bytes for length)
		int expectedSize = 12 + (4 + 2);
		assertEquals(expectedSize, msg.messageLength());
		assertEquals(expectedSize + 2, msg.toBytes().length);

		msg.setWill("WillTopic", "My Will");
		expectedSize += (9 + 2) + (7 + 2);
		assertEquals(expectedSize, msg.messageLength());
		assertEquals(expectedSize + 2, msg.toBytes().length);

		msg.setCredentials("UserName", "Secret Password");
		expectedSize += (8 + 2) + (15 + 2);
		assertEquals(expectedSize, msg.messageLength());
		assertEquals(expectedSize + 2, msg.toBytes().length);
	}

	@Test
	public void protocolIdIsCorrect() {
		ConnectMessage msg = new ConnectMessage("Test", false, 20);
		byte[] data = msg.toBytes();
		byte[] connectProtocol = Arrays.copyOfRange(data, 2, 11);
		assertArrayEquals(new byte[] { 0, 6, 'M', 'Q', 'I', 's', 'd', 'p', 3 },
				connectProtocol);
	}

	@Test
	public void cleanSessionFlagIsCorrect() {
		ConnectMessage msg = new ConnectMessage("Test", false, 20);
		byte flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(0, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);

		msg = new ConnectMessage("Test", true, 20);
		flags = msg.toBytes()[11];
		assertEquals(CLEAN_SESSION_MASK, flags & CLEAN_SESSION_MASK);
		assertEquals(0, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);
	}

	@Test
	public void willFlagsAreCorrect() {
		ConnectMessage msg = new ConnectMessage("Test", false, 20);
		msg.setWill("WillTopic", "Will text");

		byte flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(WILL_MASK, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);

		msg.setWill("WillTopic", "Will text", QoS.AT_LEAST_ONCE, false);
		flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(WILL_MASK, flags & WILL_MASK);
		assertEquals(QoS.AT_LEAST_ONCE.val << 3, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);

		msg.setWill("WillTopic", "Will text", QoS.EXACTLY_ONCE, false);
		flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(WILL_MASK, flags & WILL_MASK);
		assertEquals(QoS.EXACTLY_ONCE.val << 3, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);

		msg.setWill("WillTopic", "Will text", QoS.EXACTLY_ONCE, true);
		flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(WILL_MASK, flags & WILL_MASK);
		assertEquals(QoS.EXACTLY_ONCE.val << 3, flags & WILL_QoS_MASK);
		assertEquals(WILL_RETAIN_MASK, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);
	}
	
	@Test
	public void usernameAndPasswordFlagsAreCorrect() {
		ConnectMessage msg = new ConnectMessage("Test", false, 20);

		msg.setCredentials("Albin", null);
		byte flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(0, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(0, flags & PASSWORD_MASK);
		assertEquals(USERNAME_MASK, flags & USERNAME_MASK);

		msg.setCredentials("Albin", "password");
		flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(0, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(PASSWORD_MASK, flags & PASSWORD_MASK);
		assertEquals(USERNAME_MASK, flags & USERNAME_MASK);

		msg.setCredentials(null, "password");
		flags = msg.toBytes()[11];
		assertEquals(0, flags & CLEAN_SESSION_MASK);
		assertEquals(0, flags & WILL_MASK);
		assertEquals(0, flags & WILL_QoS_MASK);
		assertEquals(0, flags & WILL_RETAIN_MASK);
		assertEquals(PASSWORD_MASK, flags & PASSWORD_MASK);
		assertEquals(0, flags & USERNAME_MASK);
	}

}
