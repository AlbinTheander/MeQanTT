package com.albin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.albin.mqtt.NettyClient;

public class NettyMain {
	
	private static NettyClient client;
	private static String topic;

	public static void main(String[] args) throws InterruptedException, IOException {
		client = new NettyClient(args[0]);
		client.connect();
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

}
