package com.albin.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import com.albin.mqtt.message.ConnAckMessage;
import com.albin.mqtt.message.ConnectMessage;
import com.albin.mqtt.message.DisconnectMessage;
import com.albin.mqtt.message.Message;
import com.albin.mqtt.message.MessageFactory;
import com.albin.mqtt.message.PublishMessage;

public class MqttClient {
	
	private MessageFactory factory;
	private InputStream in;
	private Socket socket;
	private OutputStream out;
	private MqttReader reader;
	private Semaphore connectionAckLock;
	
	public MqttClient() {
		factory = new MessageFactory();
	}
	
	
	public void connect(String host, int port, String clientId) throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket(host, port);
		in = socket.getInputStream();
		out = socket.getOutputStream();
		reader = new MqttReader();
		reader.start();
		ConnectMessage msg = new ConnectMessage(clientId, false, 10);
		connectionAckLock = new Semaphore(0);
		out.write(msg.toBytes());
		connectionAckLock.acquire();
	}
	
	public void publish(String topic, String message) throws IOException {
		PublishMessage msg = new PublishMessage(topic, message);
		out.write(msg.toBytes());
	}
	
	public void disconnect() throws IOException {
		DisconnectMessage msg = new DisconnectMessage();
		out.write(msg.toBytes());
		socket.close();
	}

	
	private void handleMessage(Message msg) {
		switch (msg.getType()) {
		case CONNACK:
			handleMessage((ConnAckMessage) msg);
			break;
		case PUBLISH:
			handleMessage((PublishMessage) msg);
			break;
		default:
			break;
		}
	}
	
	private void handleMessage(ConnAckMessage msg) {
		connectionAckLock.release();
	}
	
	private void handleMessage(PublishMessage msg) {
		
	}
	
	private class MqttReader extends Thread {
		
		@Override
		public void run() {
			Message msg;
			try {
				msg = factory.read(in);
				handleMessage(msg);
			} catch (IOException e) {
			}
		}
	}

}
