package com.albin;

import java.io.IOException;
import java.net.UnknownHostException;

import com.albin.mqtt.SocketClient;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		SocketClient client = new SocketClient("Test");
		client.connect("localhost", 1883);
		Thread.sleep(3000);
//		client.subscribe("$SYS/#");
		Thread.sleep(1000);
		Thread.sleep(30000);
		client.disconnect();
	}

}
