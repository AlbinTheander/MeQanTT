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
package com.albin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.albin.mqtt.MqttListener;
import com.albin.mqtt.NettyClient;
import com.albin.mqtt.util.FormatUtil;

public class NettyMain {
	
	private static NettyClient client;
	private static String topic;

	public static void main(String[] args) throws InterruptedException, IOException {
		String id = args.length == 0 ? "Dummy_"+System.currentTimeMillis() : args[0];
		client = new NettyClient(id);
		client.setListener(new PrintingListener());
		client.connect("localhost", 1883);
		beInteractive();
		client.disconnect();
	}

	private static void beInteractive() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		do {
			line = in.readLine();
			if (line.startsWith("pub"))
				publish(line.substring(4));
			if (line.startsWith("sub"))
				subscribe(line.substring(4));
			if (line.startsWith("unsub"))
				unsubscribe(line.substring(6));
			if (line.startsWith("topic")) {
				topic = line.substring(6);
			}
		} while(!"bye".equals(line));
		
	}

	private static void unsubscribe(String topic) {
		client.unsubscribe(topic);
	}

	private static void subscribe(String topic) {
		client.subscribe(topic);
	}

	private static void publish(String msg) {
		client.publish(topic, msg);
	}
	
	private static class PrintingListener implements MqttListener {

		public void disconnected() {
			System.out.println("DISCONNECTED");
		}

		public void publishArrived(String topic, byte[] data) {
			System.out.println("[" + topic + "]: " + FormatUtil.toString(data));
		}
		
	}

}
