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

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;
import org.meqantt.message.Message;
import org.meqantt.message.QoS;
import org.meqantt.message.Message.Type;


public class MessageTest {

	private class ConcreteMessage extends Message {
		
		private int length;

		public ConcreteMessage(Type type) {
			super(type);
		}

		@Override
		protected int messageLength() {
			return length;
		}

		@Override
		protected void writeMessage(OutputStream out) throws IOException {
		}
	}

	@Test
	public void typeIsSetCorrectly() {
			Message msg = new ConcreteMessage(Type.CONNECT);
			assertEquals(1, getTypeValue(msg));
			msg = new ConcreteMessage(Type.CONNACK);
			assertEquals(2, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PUBLISH);
			assertEquals(3, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PUBACK);
			assertEquals(4, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PUBREC);
			assertEquals(5, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PUBREL);
			assertEquals(6, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PUBCOMP);
			assertEquals(7, getTypeValue(msg));
			msg = new ConcreteMessage(Type.SUBSCRIBE);
			assertEquals(8, getTypeValue(msg));
			msg = new ConcreteMessage(Type.SUBACK);
			assertEquals(9, getTypeValue(msg));
			msg = new ConcreteMessage(Type.UNSUBSCRIBE);
			assertEquals(10, getTypeValue(msg));
			msg = new ConcreteMessage(Type.UNSUBACK);
			assertEquals(11, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PINGREQ);
			assertEquals(12, getTypeValue(msg));
			msg = new ConcreteMessage(Type.PINGRESP);
			assertEquals(13, getTypeValue(msg));
			msg = new ConcreteMessage(Type.DISCONNECT);
			assertEquals(14, getTypeValue(msg));
		}
	
	@Test
	public void retainIsSetCorrectly() {
		Message msg = new ConcreteMessage(Type.CONNECT);
		assertNotRetained(msg);
		msg.setRetained(true);
		assertRetained(msg);
	}
	
	@Test
	public void qosIsSetCorrectly() {
		Message msg = new ConcreteMessage(Type.CONNECT);
		assertEquals(0, getQoSValue(msg));
		msg.setQos(QoS.AT_LEAST_ONCE);
		assertEquals(1, getQoSValue(msg));
		msg.setQos(QoS.AT_MOST_ONCE);
		assertEquals(0, getQoSValue(msg));
		msg.setQos(QoS.EXACTLY_ONCE);
		assertEquals(2, getQoSValue(msg));
	}
	
	@Test
	public void dupFlagIsSetCorrectly() {
		Message msg= new ConcreteMessage(Type.CONNECT);
		byte[] bytes = msg.toBytes();
		assertEquals(0, bytes[0] & 8);
		msg.setDup(true);
		bytes = msg.toBytes();
		assertEquals(8, bytes[0] & 8);
	}
	
	@Test
	public void lengthIsEncodedCorrectly() {
		ConcreteMessage msg = new ConcreteMessage(Type.CONNECT);
		msg.length = 0;
		assertLengthField(msg, 0);
		msg.length = 1;
		assertLengthField(msg, 1);
		msg.length = 127;
		assertLengthField(msg, 127);
		msg.length = 128;
		assertLengthField(msg, 0x80, 0x01);
		msg.length = 259; // 3 + 2 * 128
		assertLengthField(msg, 0x83, 0x02);
		msg.length = 16383; // 127 + 127 * 128
		assertLengthField(msg, 0xff, 0x7f);
		msg.length = 16384; // 0 + 0 * 128 + 1*128^2
		assertLengthField(msg, 0x80, 0x80, 0x01);
		msg.length = 82695;  // 7 + 6 * 128 + 5 * 128^2
		assertLengthField(msg, 0x87, 0x86, 0x05);
		msg.length = 2097151; // 127 + 127*128 + 127*128^2
		assertLengthField(msg, 0xff, 0xff, 0x7f);
		msg.length = 2097152; // 0 + 0 * 128 + 0*128^2 + 1*128^3
		assertLengthField(msg, 0x80, 0x80, 0x80, 0x01);
		msg.length = 19006342; // 6 + 7 * 128 + 8*128^2 + 9*128^3
		assertLengthField(msg, 0x86, 0x87, 0x88, 0x09);
		msg.length = 268435455; // 127 + 127*128 + 127*128^2 + 127*128^3
		assertLengthField(msg, 0xff, 0xff, 0xff, 0x7f);
	}

	private void assertLengthField(ConcreteMessage msg, int... expected) {
		byte[] bytes = msg.toBytes();
		assertEquals("Length header has wrong size", expected.length, bytes.length - 1);
		for(int i = 0; i < expected.length; i++) {
			byte exp = (byte) expected[i];
			assertEquals("Mismatch in length field on index " + i, exp, bytes[i+1]);
		}
	}

	private Object getTypeValue(Message msg) {
		byte[] bytes = msg.toBytes();
		return (bytes[0] & 0xff) >>> 4;
	}
	
	private int getQoSValue(Message msg) {
		byte[] bytes = msg.toBytes();
		int qos = (bytes[0] & 6) >> 1;
		return qos;
	}

	private void assertNotRetained(Message msg) {
		byte[] bytes = msg.toBytes();
		assertEquals(0, bytes[0] & 1);
	}

	private void assertRetained(Message msg) {
		byte[] bytes = msg.toBytes();
		assertEquals(1, bytes[0] & 1);
	}

}
