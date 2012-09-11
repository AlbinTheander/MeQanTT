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
package org.meqantt.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.meqantt.MqttListener;
import org.meqantt.message.ConnAckMessage;
import org.meqantt.message.Message;
import org.meqantt.message.PublishMessage;


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
