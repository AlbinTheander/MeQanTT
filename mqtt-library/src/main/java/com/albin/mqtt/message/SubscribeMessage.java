package com.albin.mqtt.message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.albin.mqtt.util.FormatUtil;

public class SubscribeMessage extends RetryableMessage {
	
	private List<String> topics = new ArrayList<String>();
	private List<QoS> topicQoSs = new ArrayList<QoS>();

	public SubscribeMessage(String topic, QoS topicQos) {
		super(Type.SUBSCRIBE);
		setQos(QoS.AT_LEAST_ONCE);
		topics.add(topic);
		topicQoSs.add(topicQos);
	}
	
	@Override
	protected int messageLength() {
		int length = 2; // message id length
		for(String topic: topics) {
			length += FormatUtil.toMQttString(topic).length;
			length += 1; // topic QoS
		}
		return length;
	}
	
	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		super.writeMessage(out);
		DataOutputStream dos = new DataOutputStream(out);
		for(int i = 0; i < topics.size(); i++) {
			dos.writeUTF(topics.get(i));
			dos.write(topicQoSs.get(i).val);
		}
		dos.flush();
	}
	

	@Override
	public void setQos(QoS qos) {
		if (qos != QoS.AT_LEAST_ONCE) {
			throw new IllegalArgumentException(
					"SUBSCRIBE is always using QoS-level AT LEAST ONCE. Requested level: "
							+ qos);
		}
		super.setQos(qos);
	}
	
	@Override
	public void setDup(boolean dup) {
		if (dup == true) {
			throw new IllegalArgumentException("SUBSCRIBE can't set the DUP flag.");
		}
		super.setDup(dup);
	}
	
	@Override
	public void setRetained(boolean retain) {
		throw new UnsupportedOperationException("SUBSCRIBE messages don't use the RETAIN flag");
	}

}
