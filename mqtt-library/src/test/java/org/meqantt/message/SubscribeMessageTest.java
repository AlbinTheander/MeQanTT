package org.meqantt.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.meqantt.message.MessageInputStream;
import org.meqantt.message.QoS;
import org.meqantt.message.SubscribeMessage;

public class SubscribeMessageTest {

	@Test
	public void isDeserializedCorrectly() throws IOException {
		SubscribeMessage msg = new SubscribeMessage("/test/topic/1",
				QoS.AT_LEAST_ONCE);
		msg.addTopic("/test/topic/2", QoS.AT_MOST_ONCE);
		byte[] data = msg.toBytes();
		MessageInputStream in = new MessageInputStream(
				new ByteArrayInputStream(data));
		SubscribeMessage smsg=null;
		try {
			smsg=(SubscribeMessage)in.readMessage();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		List<String> topics=smsg.getTopics();
		List<QoS> topicQoSs=smsg.getTopicQoSs();
		Assert.assertEquals(2, topics.size());
		Assert.assertEquals(2, topicQoSs.size());
		Assert.assertEquals("/test/topic/1", topics.get(0));
		Assert.assertEquals("/test/topic/2", topics.get(1));
		Assert.assertEquals(QoS.AT_LEAST_ONCE, topicQoSs.get(0));
		Assert.assertEquals(QoS.AT_MOST_ONCE, topicQoSs.get(1));
	}

}
