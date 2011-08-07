package com.albin.mqtt;

public interface MqttListener {
	
	void disconnected();
	
	void publishArrived(String topic, byte[] data);

}
