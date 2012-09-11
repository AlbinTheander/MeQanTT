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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.meqantt.message.Message;


public class MqttMessageEncoder extends OneToOneEncoder implements
		ChannelHandler {

	@Override
	protected Object encode(ChannelHandlerContext ctc, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof Message)) {
			return null;
		}
		byte[] data = ((Message) msg).toBytes();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(data); // data
		return buf;
	}

}
