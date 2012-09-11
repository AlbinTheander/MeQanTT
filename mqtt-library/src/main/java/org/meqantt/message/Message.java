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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Message {
	// @formatter:off
	public enum Type {
		CONNECT     ( 1),
		CONNACK     ( 2),
		PUBLISH     ( 3),
		PUBACK      ( 4),
		PUBREC      ( 5),
		PUBREL      ( 6),
		PUBCOMP     ( 7),
		SUBSCRIBE   ( 8),
		SUBACK      ( 9),
		UNSUBSCRIBE (10),
		UNSUBACK    (11),
		PINGREQ     (12),
		PINGRESP    (13),
		DISCONNECT  (14);

		final private int val;
		
		Type(int val) {
			this.val = val;
		}
		
		static Type valueOf(int i) {
			for(Type t: Type.values()) {
				if (t.val == i)
					return t;
			}
			return null;
		}
	}
	
	public static class Header {
		
		private Type type;
		private boolean retain;
		private QoS qos = QoS.AT_MOST_ONCE;
		private boolean dup;
		
		private Header(Type type, boolean retain, QoS qos, boolean dup) {
			this.type = type;
			this.retain = retain;
			this.qos = qos;
			this.dup = dup;
		}

		public Header(byte flags) {
			retain = (flags & 1) > 0;
			qos = QoS.valueOf((flags & 0x6) >> 1);
			dup = (flags & 8) > 0;
			type = Type.valueOf((flags >> 4) & 0xF);
		}
		
		public Type getType() {
			return type;
		}

		private byte encode() {
			byte b = 0;
			b = (byte) (type.val << 4);
			b |= retain ? 1 : 0;
			b |= qos.val << 1;
			b |= dup ? 8 : 0;
			return b;
		}

		@Override
		public String toString() {
			return "Header [type=" + type + ", retain=" + retain + ", qos="
					+ qos + ", dup=" + dup + "]";
		}

		
	}
	
	private static char nextId = 1;
	
	private final Header header;


	public Message(Type type) {
		header = new Header(type, false, QoS.AT_MOST_ONCE, false);
	}
	
	public Message(Header header) throws IOException {
		this.header = header;
	}

	final void read(InputStream in) throws IOException {
		int msgLength = readMsgLength(in);
		readMessage(in, msgLength);
	}
	
	public final void write(OutputStream out) throws IOException {
		out.write(header.encode());
		writeMsgLength(out);
		writeMessage(out);
	}

	private int readMsgLength(InputStream in) throws IOException {
		int msgLength = 0;
		int multiplier = 1;
		int digit;
		do {
			digit = in.read();
			msgLength += (digit & 0x7f) * multiplier;
			multiplier *= 128;
		} while ((digit & 0x80) > 0);
		return msgLength;
	}
	
	private void writeMsgLength(OutputStream out) throws IOException {
		int msgLength = messageLength();
		int val = msgLength;
		do {
			byte b = (byte) (val & 0x7F);
			val >>= 7;
			if (val > 0) {
				b |= 0x80;
			}
			out.write(b);
		} while (val > 0);
	}

	public final byte[] toBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			write(baos);
		} catch (IOException e) {
		}
		return baos.toByteArray();
	}

	protected int messageLength() {
		return 0;
	}
	
	protected void writeMessage(OutputStream out) throws IOException {
	}
	
	protected void readMessage(InputStream in, int msgLength) throws IOException {
		
	}

	public void setRetained(boolean retain) {
		header.retain = retain;
	}

	public boolean isRetained() {
		return header.retain;
	}

	public void setQos(QoS qos) {
		header.qos = qos;
	}

	public QoS getQos() {
		return header.qos;
	}

	public void setDup(boolean dup) {
		header.dup = dup;
	}

	public boolean isDup() {
		return header.dup;
	}

	public Type getType() {
		return header.type;
	}
	
	public static char nextId() {
		return nextId++;
	}

}
