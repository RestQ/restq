/**
 * 
 */
package org.restq.messaging.impl;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.Subscriber;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class TopicImplTest {

	@Test
	public void shouldWriteToDataOutput() throws IOException {
		TopicImpl topic = new TopicImpl("id", "testtopic");
		DataOutputWrapper output = mock(DataOutputWrapper.class);
		topic.writeData(output);
		InOrder order = inOrder(output);
		order.verify(output).writeString(topic.getName());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		TopicImpl topic = new TopicImpl();
		DataInputWrapper input = mock(DataInputWrapper.class);
		final String name = "testtopic";
		final String id = "id";
		doAnswer(new Answer<String>() {
			private int count;
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return count++ == 0 ? id : name;
			}
		}).when(input).readString();
		topic.readData(input);
		InOrder order = inOrder(input);
		order.verify(input, times(2)).readString();
		assertEquals(topic.getName(), name);
	}
	
	@Test
	public void shouldAddMessageToQueue() {
		TopicImpl topic = new TopicImpl("id", "testtopic");
		ServerMessage msg = mock(ServerMessage.class);
		topic.addMessage(msg);
	}
	
	@Test
	public void shouldAddSubscriber() {
		TopicImpl topic = new TopicImpl("id", "testtopic");
		Subscriber subscriber = mock(Subscriber.class);
		topic.addSubscriber(subscriber);
		assertTrue(topic.getSubscribers().contains(subscriber));
	}
	
	@Test
	public void shouldRemoveSubscriber() {
		TopicImpl topic = new TopicImpl("id", "testtopic");
		Subscriber subscriber = mock(Subscriber.class);
		topic.addSubscriber(subscriber);
		topic.removeSubscriber(subscriber);
		assertFalse(topic.getSubscribers().contains(subscriber));
	}
	
	@Test
	public void shouldGetSubscribers() {
		TopicImpl topic = new TopicImpl("id", "testtopic");
		Subscriber subscriber1 = mock(Subscriber.class);
		Subscriber subscriber2 = mock(Subscriber.class);
		topic.addSubscriber(subscriber1);
		topic.addSubscriber(subscriber2);
		assertEquals(topic.getSubscribers().size(), 2);
	}
}
