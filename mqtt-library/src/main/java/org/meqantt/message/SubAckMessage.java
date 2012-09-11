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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SubAckMessage extends RetryableMessage {

	private List<QoS> grantedQoSs;
	
	public SubAckMessage(){
		super(Type.SUBACK);
	}
	
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

	@Override
	protected int messageLength() {
		return grantedQoSs == null ? 2 : 2 + grantedQoSs.size();
	}
	
	@Override
	protected void writeMessage(OutputStream out) throws IOException {
		super.writeMessage(out);
		if(grantedQoSs!=null){
			for(QoS qos:grantedQoSs){
				out.write(qos.val);
			}
		}
	}

	public void addQoS(QoS qos) {
		if (grantedQoSs == null) {
			grantedQoSs = new ArrayList<QoS>();
		}
		grantedQoSs.add(qos);
	}

	public List<QoS> getGrantedQoSs() {
		return grantedQoSs;
	}

	@Override
	public void setDup(boolean dup) {
		throw new UnsupportedOperationException(
				"SubAck messages don't use the DUP flag.");
	}

	@Override
	public void setRetained(boolean retain) {
		throw new UnsupportedOperationException(
				"SubAck messages don't use the RETAIN flag.");
	}

	@Override
	public void setQos(QoS qos) {
		throw new UnsupportedOperationException(
				"SubAck messages don't use the QoS flags.");
	}

}
