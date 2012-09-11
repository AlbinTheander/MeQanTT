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

import java.io.ByteArrayInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.meqantt.message.Message;
import org.meqantt.message.MessageInputStream;


public class MqttMessageDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buf) throws Exception {
		if (buf.readableBytes() < 2) {
			return null;
		}
		buf.markReaderIndex();
		buf.readByte(); // read away header
		int msgLength = 0;
		int multiplier = 1;
		int digit;
		int lengthSize = 0;
		do {
			lengthSize++;
			digit = buf.readByte();
			msgLength += (digit & 0x7f) * multiplier;
			multiplier *= 128;
			if ((digit & 0x80) > 0 && !buf.readable()) {
				buf.resetReaderIndex();
				return null;
			}
		} while ((digit & 0x80) > 0);
		if (buf.readableBytes() < msgLength) {
			buf.resetReaderIndex();
			return null;
		}
		byte[] data = new byte[1 + lengthSize + msgLength];
		buf.resetReaderIndex();
		buf.readBytes(data);
		MessageInputStream mis = new MessageInputStream(
				new ByteArrayInputStream(data));
		Message msg = mis.readMessage();
		return msg;
	}

}
