package com.albin.mqtt.message;

public class DisconnectMessage extends Message {

	public DisconnectMessage() {
		super(Type.DISCONNECT);
	}
	
	@Override
	public void setDup(boolean dup) {
		throw new UnsupportedOperationException("DISCONNECT message does not support the DUP flag");
	}
	
	@Override
	public void setQos(QoS qos) {
		throw new UnsupportedOperationException("DISCONNECT message does not support the QoS flag");
	}
	
	@Override
	public void setRetained(boolean retain) {
		throw new UnsupportedOperationException("DISCONNECT message does not support the RETAIN flag");
	}

}
