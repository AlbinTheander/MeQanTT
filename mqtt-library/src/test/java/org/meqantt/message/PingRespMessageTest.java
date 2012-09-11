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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.meqantt.message.PingRespMessage;
import org.meqantt.message.Message.Header;



public class PingRespMessageTest {
	
	@Test
	public void isDeserializedCorrectly() throws IOException {
		Header header = new Header((byte) 0xD0);
		InputStream in = new ByteArrayInputStream(new byte[] {0});
		PingRespMessage msg = new PingRespMessage(header);
		msg.read(in);
	}

}
