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
import com.albin.mqtt.message.MessageInputStream;
import com.albin.mqtt.message.MessageOutputStream;
import com.albin.mqtt.message.PublishMessage;
import com.albin.mqtt.message.QoS;
import com.albin.mqtt.message.SubscribeMessage;

public class MqttClient {

	private MessageInputStream in;
	private Socket socket;
	private MessageOutputStream out;
	private MqttReader reader;
	private Semaphore connectionAckLock;

	public MqttClient() {
	}

	public void connect(String host, int port, String clientId)
			throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket(host, port);
		InputStream is = socket.getInputStream();
		in = new MessageInputStream(is);
		OutputStream os = socket.getOutputStream();
		out = new MessageOutputStream(os);
		reader = new MqttReader();
		reader.start();
		ConnectMessage msg = new ConnectMessage(clientId, false, 60);
		connectionAckLock = new Semaphore(0);
		out.writeMessage(msg);
		connectionAckLock.acquire();
	}

	public void publish(String topic, String message) throws IOException {
		PublishMessage msg = new PublishMessage(topic, message);
		out.writeMessage(msg);
	}

	public void subscribe(String topic) throws IOException {
		SubscribeMessage msg = new SubscribeMessage(topic, QoS.AT_MOST_ONCE);
		out.writeMessage(msg);
	}

	public void disconnect() throws IOException {
		DisconnectMessage msg = new DisconnectMessage();
		out.writeMessage(msg);
		socket.close();
	}

	
	private void handleMessage(Message msg) {
		if (msg == null) {
			return;
		}
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
				while (true) {
					msg = in.readMessage();
					handleMessage(msg);
				}
			} catch (IOException e) {
			}
		}
	}

}
