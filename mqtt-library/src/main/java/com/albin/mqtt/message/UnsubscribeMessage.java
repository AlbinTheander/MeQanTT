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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.albin.mqtt.util.FormatUtil;

public class UnsubscribeMessage extends RetryableMessage {
	
	private List<String> topics = new ArrayList<String>();

	public UnsubscribeMessage(String topic) {
		super(Type.UNSUBSCRIBE);
		setQos(QoS.AT_LEAST_ONCE);
		topics.add(topic);
	}
	
	@Override
	protected int messageLength() {
		int length = 2; // message id length
		for(String topic: topics) {
			length += FormatUtil.toMQttString(topic).length;
		}
		return length;
	}
	
	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		super.writeMessage(dos);
		for(String topic: topics) {
			dos.writeUTF(topic);
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
