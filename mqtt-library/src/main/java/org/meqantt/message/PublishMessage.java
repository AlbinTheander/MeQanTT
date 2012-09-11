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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.meqantt.util.FormatUtil;


public class PublishMessage extends RetryableMessage {

	private String topic;
	private byte[] data;

	public PublishMessage(String topic, String msg) {
		this(topic, FormatUtil.toMQttString(msg));
	}
	
	public PublishMessage(String topic, byte[] data) {
		super(Type.PUBLISH);
		this.topic = topic;
		this.data = data;
	}

	public PublishMessage(Header header) throws IOException {
		super(header);
	}
	
	
	@Override
	protected int messageLength() {
		int length = FormatUtil.toMQttString(topic).length;
		length += (getQos() == QoS.AT_MOST_ONCE) ? 0 : 2;
		length += data.length;
		return length;
	}
	
	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeUTF(topic);
		dos.flush();
		if (getQos() != QoS.AT_MOST_ONCE) {
			super.writeMessage(out);
		}
		dos.write(data);
		dos.flush();
	}
	
	@Override
	protected void readMessage(InputStream in, int msgLength)
			throws IOException {
		int pos = 0;
		DataInputStream dis = new DataInputStream(in);
		topic = dis.readUTF();
		pos += FormatUtil.toMQttString(topic).length;
		if (getQos() != QoS.AT_MOST_ONCE) {
			super.readMessage(in, msgLength);
			pos += 2;
		}
		data = new byte[msgLength - pos];
		dis.read(data);
	}
	
	public String getTopic() {
		return topic;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public String getDataAsString() {
		return new String(data);
	}

}
