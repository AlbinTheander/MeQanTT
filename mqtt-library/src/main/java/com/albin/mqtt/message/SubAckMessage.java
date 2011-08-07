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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubAckMessage extends RetryableMessage {
	
	private List<QoS> grantedQoSs;

	public SubAckMessage(Header header) throws IOException {
		super(header);
	}
	
	@Override
	protected void readMessage(InputStream in, int msgLength)
			throws IOException {
		super.readMessage(in, msgLength);
		int pos = 2;
		while (pos < msgLength) {
			QoS qos = QoS.valueOf(in.read());
			addQoS(qos);
			pos++;
		}
	}

	private void addQoS(QoS qos) {
		if (grantedQoSs == null) {
			grantedQoSs = new ArrayList<QoS>();
		}
		grantedQoSs.add(qos);
	}
	
	public List<QoS> getGrantedQoSs() {
		return grantedQoSs;
	}
	

}
