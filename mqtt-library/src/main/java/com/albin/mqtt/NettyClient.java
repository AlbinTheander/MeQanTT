package com.albin.mqtt;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.albin.mqtt.message.ConnectMessage;
import com.albin.mqtt.message.DisconnectMessage;
import com.albin.mqtt.message.PublishMessage;
import com.albin.mqtt.message.QoS;
import com.albin.mqtt.message.SubscribeMessage;
import com.albin.mqtt.message.UnsubscribeMessage;

public class NettyClient {

	private Channel channel;
	private ClientBootstrap bootstrap;
	private final String id;
	
	public NettyClient(String id) {
		this.id = id;
	}

	public void connect() {
		bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new MqttMessageEncoder(),
						new MqttMessageDecoder(), new MqttMessageHandler());
			}
		});

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"localhost", 1883));

		channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		
		channel.write(new ConnectMessage(id, true, 30));
	}
	
	public void disconnect() {
		channel.write(new DisconnectMessage()).awaitUninterruptibly();
		channel.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}

	public void subscribe(String topic) {
		channel.write(new SubscribeMessage(topic, QoS.AT_MOST_ONCE));
	}

	public void unsubscribe(String topic) {
		channel.write(new UnsubscribeMessage(topic));
	}
	
	public void publish(String topic, String msg) {
		channel.write(new PublishMessage(topic, msg));
	}

}
