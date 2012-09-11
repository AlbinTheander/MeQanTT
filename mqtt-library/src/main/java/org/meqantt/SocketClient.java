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
package org.meqantt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import org.meqantt.message.ConnAckMessage;
import org.meqantt.message.ConnectMessage;
import org.meqantt.message.DisconnectMessage;
import org.meqantt.message.Message;
import org.meqantt.message.MessageInputStream;
import org.meqantt.message.MessageOutputStream;
import org.meqantt.message.PublishMessage;
import org.meqantt.message.QoS;
import org.meqantt.message.SubscribeMessage;


public class SocketClient {

	private MessageInputStream in;
	private Socket socket;
	private MessageOutputStream out;
	private MqttReader reader;
	private Semaphore connectionAckLock;
	private final String id;

	public SocketClient(String id) {
		this.id = id;
	}

	public void connect(String host, int port)
			throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket(host, port);
		InputStream is = socket.getInputStream();
		in = new MessageInputStream(is);
		OutputStream os = socket.getOutputStream();
		out = new MessageOutputStream(os);
		reader = new MqttReader();
		reader.start();
		ConnectMessage msg = new ConnectMessage(id, false, 60);
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
		System.out.println("PUBLISH (" + msg.getTopic() + "): "
				+ msg.getDataAsString());
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
