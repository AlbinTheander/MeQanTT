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
