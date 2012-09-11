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
import java.io.DataInputStream;
import java.io.IOException;

import org.junit.Test;
import org.meqantt.message.PublishMessage;
import org.meqantt.message.QoS;


public class PublishMessageTest {
	
	@Test
	public void messageLengthIsCorrect() {
		PublishMessage msg = new PublishMessage("Topic", "Message");
		int expectedLength = (5+2) + (7+2);
		assertEquals(expectedLength, msg.messageLength());
		
		msg.setQos(QoS.AT_MOST_ONCE);
		assertEquals(expectedLength, msg.messageLength());

		
		msg.setQos(QoS.AT_LEAST_ONCE);
		expectedLength += 2;
		assertEquals(expectedLength, msg.messageLength());
		
		msg.setQos(QoS.EXACTLY_ONCE);
		assertEquals(expectedLength, msg.messageLength());
	}
	
	@Test
	public void serializationWorks() throws IOException {
		PublishMessage msg = new PublishMessage("Topic", "Message");
		byte[] data = msg.toBytes();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		// Read away 2-byte generic header
		dis.read();
		dis.read();
		String topic = dis.readUTF();
		assertEquals("Topic", topic);
		String message = dis.readUTF();
		assertEquals("Message", message);
		
		msg.setQos(QoS.EXACTLY_ONCE);
		msg.setMessageId(1);
		data = msg.toBytes();
		dis = new DataInputStream(new ByteArrayInputStream(data));
		// Read away 2-byte generic header
		dis.readShort();
		topic = dis.readUTF();
		assertEquals("Topic", topic);
		int messageId = dis.readShort();
		assertEquals(1, messageId);
		message = dis.readUTF();
		assertEquals("Message", message);
	}

}
