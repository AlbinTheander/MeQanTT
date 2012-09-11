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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.albin.mqtt.util.FormatUtil;

public class ConnectMessage extends Message {

	private static int CONNECT_HEADER_SIZE = 12;

	private String protocolId = "MQIsdp";
	private byte protocolVersion = 3;
	private String clientId;
	private int keepAlive;
	private String username;
	private String password;
	private boolean cleanSession;
	private String willTopic;
	private String will;
	private QoS willQoS = QoS.AT_MOST_ONCE;
	private boolean retainWill = false;
	private boolean usernameFlag;
	private boolean passwordFlag;
	private boolean willFlag;
	
	public ConnectMessage() {
		super(Type.CONNECT);
	}

	public ConnectMessage(Header header) throws IOException {
		super(header);
	}

	public ConnectMessage(String clientId, boolean cleanSession, int keepAlive) {
		super(Type.CONNECT);
		if (clientId == null || clientId.length() > 23) {
			throw new IllegalArgumentException(
					"Client id cannot be null and must be at most 23 characters long: "
							+ clientId);
		}
		this.clientId = clientId;
		this.cleanSession = cleanSession;
		this.keepAlive = keepAlive;
	}

	@Override
	protected int messageLength() {
		int payloadSize = FormatUtil.toMQttString(clientId).length;
		payloadSize += FormatUtil.toMQttString(willTopic).length;
		payloadSize += FormatUtil.toMQttString(will).length;
		payloadSize += FormatUtil.toMQttString(username).length;
		payloadSize += FormatUtil.toMQttString(password).length;
		return payloadSize + CONNECT_HEADER_SIZE;
	}

	@Override
	protected void readMessage(InputStream in, int msgLength)
			throws IOException {
		DataInputStream dis = new DataInputStream(in);
		protocolId = dis.readUTF();
		protocolVersion = dis.readByte();
		byte cFlags = dis.readByte();
		usernameFlag = (cFlags & 0x80) > 0;
		passwordFlag = (cFlags & 0x40) > 0;
		retainWill = (cFlags & 0x20) > 0;
		willQoS = QoS.valueOf(cFlags >> 3 & 0x03);
		willFlag = (cFlags & 0x04) > 0;
		cleanSession = (cFlags & 0x20) > 0;
		keepAlive = dis.read() * 256 + dis.read();
		clientId = dis.readUTF();
		if (willFlag) {
			willTopic = dis.readUTF();
			will = dis.readUTF();
		}
		if (usernameFlag) {
			username = dis.readUTF();
		}
		if (passwordFlag) {
			password = dis.readUTF();
		}

	}

	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeUTF(protocolId);
		dos.write(protocolVersion);
		int flags = cleanSession ? 2 : 0;
		flags |= willFlag ? 0x04 : 0;
		flags |= willQoS.val << 3;
		flags |= retainWill ? 0x20 : 0;
		flags |= passwordFlag ? 0x40 : 0;
		flags |= usernameFlag ? 0x80 : 0;
		dos.write((byte) flags);
		dos.writeChar(keepAlive);

		dos.writeUTF(clientId);
		if (willFlag) {
			dos.writeUTF(willTopic);
			dos.writeUTF(will);
		}
		if (usernameFlag) {
			dos.writeUTF(username);
		}
		if (passwordFlag) {
			dos.writeUTF(password);
		}
		dos.flush();
	}

	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
		usernameFlag = this.username != null;
		passwordFlag = this.password != null;
	}

	public void setWill(String willTopic, String will) {
		this.willTopic = willTopic;
		this.will = will;

		willFlag = this.will != null;
	}

	public void setWill(String willTopic, String will, QoS willQoS,
			boolean retainWill) {
		this.willTopic = willTopic;
		this.will = will;
		this.willQoS = willQoS;
		this.retainWill = retainWill;
		willFlag = this.will != null;
	}

	@Override
	public void setDup(boolean dup) {
		throw new UnsupportedOperationException(
				"CONNECT messages don't use the DUP flag.");
	}

	@Override
	public void setRetained(boolean retain) {
		throw new UnsupportedOperationException(
				"CONNECT messages don't use the RETAIN flag.");
	}

	@Override
	public void setQos(QoS qos) {
		throw new UnsupportedOperationException(
				"CONNECT messages don't use the QoS flags.");
	}
	
	public String getProtocolId() {
		return protocolId;
	}

	public byte getProtocolVersion() {
		return protocolVersion;
	}

	public String getClientId() {
		return clientId;
	}

	public int getKeepAlive() {
		return keepAlive;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public String getWillTopic() {
		return willTopic;
	}

	public String getWill() {
		return will;
	}

	public QoS getWillQoS() {
		return willQoS;
	}

	public boolean isRetainWill() {
		return retainWill;
	}

	public boolean isUsernameFlag() {
		return usernameFlag;
	}

	public boolean isPasswordFlag() {
		return passwordFlag;
	}

	public boolean isWillFlag() {
		return willFlag;
	}

}
