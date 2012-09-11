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
package org.meqantt.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.meqantt.message.Message.Header;


public class MessageInputStream implements Closeable {

	private InputStream in;

	public MessageInputStream(InputStream in) {
		this.in = in;
	}

	public Message readMessage() throws IOException {
		byte flags = (byte) in.read();
		Header header = new Header(flags);
		Message msg = null;
		switch (header.getType()) {
		case CONNACK:
			msg = new ConnAckMessage(header);
			break;
		case PUBLISH:
			msg = new PublishMessage(header);
			break;
		case PUBACK:
			msg = new PubAckMessage(header);
			break;
		case PUBREC:
			msg = new PubRecMessage(header);
			break;
		case PUBREL:
			msg = new PubRelMessage(header);
			break;
		case PUBCOMP:
			msg = new PubCompMessage(header);
			break;
		case SUBACK:
			msg = new SubAckMessage(header);
			break;
		case UNSUBACK:
			msg = new UnsubAckMessage(header);
			break;
		case PINGRESP:
			msg = new PingRespMessage(header);
			break;
		case CONNECT:
			msg = new ConnectMessage(header);
			break;
		case SUBSCRIBE:
			msg=new SubscribeMessage(header);
			break;
		case UNSUBSCRIBE:
			msg=new UnsubscribeMessage(header);
			break;
		case PINGREQ:
			msg=new PingReqMessage(header);
			break;
		case DISCONNECT:
			msg=new DisconnectMessage(header);
			break;
		default:
			throw new UnsupportedOperationException(
					"No support for deserializing " + header.getType()
							+ " messages");
		}
		msg.read(in);
		return msg;
	}

	public void close() throws IOException {
		in.close();
	}
}
