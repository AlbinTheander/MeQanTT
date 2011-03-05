package com.albin.mqtt.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.albin.mqtt.util.FormatUtil;

public class PublishMessage extends Message {

	private String topic;
	private byte[] data;
	private int messageId = -1;

	public PublishMessage(String topic, String msg) {
		this(topic, FormatUtil.toMQttString(msg));
	}
	
	public PublishMessage(Header header, InputStream in) throws IOException {
		super(header, in);
	}
	
	public PublishMessage(String topic, byte[] data) {
		super(Type.PUBLISH);
		this.topic = topic;
		this.data = data;
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
		if (messageId == -1) {
			messageId = nextId();
		}
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeUTF(topic);
		if (getQos() != QoS.AT_MOST_ONCE) {
			dos.writeChar(messageId);
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
			messageId = dis.readChar();
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
		return FormatUtil.toString(data);
	}

}
