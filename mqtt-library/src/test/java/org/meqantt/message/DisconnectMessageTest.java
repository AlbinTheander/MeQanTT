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

import org.junit.Before;
import org.junit.Test;
import org.meqantt.message.DisconnectMessage;
import org.meqantt.message.QoS;

public class DisconnectMessageTest {

	private DisconnectMessage msg;

	@Before
	public void setUp() {
		msg = new DisconnectMessage();
	}

	@Test
	public void messageIsCorrectlyEncoded() {
		byte[] data = msg.toBytes();
		assertEquals(0xE0, data[0] & 0xF0); // messageId = 14
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
