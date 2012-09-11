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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.meqantt.message.QoS;
import org.meqantt.message.SubAckMessage;
import org.meqantt.message.Message.Header;



public class SubAckMessageTest {
	
	@Test
	public void isDeserializedCorrectly() throws IOException {
		Header header = new Header((byte) 0x90);
		InputStream in = new ByteArrayInputStream(new byte[] {5, 0, 5, 2, 0, 1});
		SubAckMessage msg = new SubAckMessage(header);
		msg.read(in);
		assertEquals(5, msg.getMessageId());
		List<QoS> qoses = msg.getGrantedQoSs();
		assertEquals(3, qoses.size());
		assertEquals(QoS.EXACTLY_ONCE, qoses.get(0));
		assertEquals(QoS.AT_MOST_ONCE, qoses.get(1));
		assertEquals(QoS.AT_LEAST_ONCE, qoses.get(2));
	}
	
	@Test
	public void isSerializedCorrectly() throws IOException{
		SubAckMessage msg=new SubAckMessage();
		msg.addQoS(QoS.AT_LEAST_ONCE);
		msg.addQoS(QoS.AT_MOST_ONCE);
		msg.addQoS(QoS.EXACTLY_ONCE);
		byte[] data=msg.toBytes();
		assertEquals(7,data.length);
		assertEquals(5, msg.messageLength());
		assertEquals(0x01, data[4]);
		assertEquals(0x00, data[5]);
		assertEquals(0x02, data[6]);
	}

}
