package com.albin.mqtt.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.albin.mqtt.MqttListener;
import com.albin.mqtt.message.ConnAckMessage;
import com.albin.mqtt.message.Message;
import com.albin.mqtt.message.PublishMessage;

public class MqttMessageHandler extends SimpleChannelHandler {
	
	private MqttListener listener;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		System.out.println("Caught exception: " + e.getCause());
		e.getChannel().close();
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		if (listener != null) {
			listener.disconnected();
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		handleMessage((Message) e.getMessage());
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
		// What to do here?
	}

	private void handleMessage(PublishMessage msg) {
		if (listener != null) {
			listener.publishArrived(msg.getTopic(), msg.getData());
		}
	}

	public void setListener(MqttListener listener) {
		this.listener = listener;
	}


}
