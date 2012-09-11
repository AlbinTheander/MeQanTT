package org.meqantt.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.meqantt.message.MessageInputStream;
import org.meqantt.message.UnsubscribeMessage;

public class UnsubscribeMessageTest {

	@Test
	public void isDeserializedCorrectly() throws IOException {
		UnsubscribeMessage msg = new UnsubscribeMessage("topic/1");
		msg.addTopic("topic/2");
		msg.addTopic("topic/3");
		byte[] data = msg.toBytes();
		MessageInputStream in = new MessageInputStream(
				new ByteArrayInputStream(data));
		UnsubscribeMessage umsg = null;
		try {
			umsg = (UnsubscribeMessage) in.readMessage();
		} catch (Exception e) {
			assertTrue(false);
		}
		assertEquals(3, umsg.getTopics().size());
		assertEquals("topic/1", umsg.getTopics().get(0));
		assertEquals("topic/2", umsg.getTopics().get(1));
		assertEquals("topic/3", umsg.getTopics().get(2));
	}

}
