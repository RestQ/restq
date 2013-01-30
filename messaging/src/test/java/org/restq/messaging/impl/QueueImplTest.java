/**
 * 
 */
package org.restq.messaging.impl;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mockito.InOrder;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.utility.PriorityQueue;
import org.restq.messaging.ServerMessage;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class QueueImplTest {
	
	@Test
	public void shouldWriteToDataOutput() throws IOException {
		QueueImpl queue = new QueueImpl("testqueue", true);
		DataOutputWrapper output = mock(DataOutputWrapper.class);
		queue.writeData(output);
		InOrder order = inOrder(output);
		order.verify(output).writeString(queue.getName());
		order.verify(output).writeBoolean(queue.isDurable());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		QueueImpl queue = new QueueImpl();
		DataInputWrapper input = mock(DataInputWrapper.class);
		final String name = "testQueue";
		when(input.readString()).thenReturn(name);
		when(input.readBoolean()).thenReturn(true);
		queue.readData(input);
		InOrder order = inOrder(input);
		order.verify(input).readString();
		order.verify(input).readBoolean();
		assertEquals(queue.getName(), name);
		assertEquals(queue.isDurable(), true);
	}
	
	@Test
	public void shouldAddMessageToQueue() {
		PriorityQueue<ServerMessage> pq = mock(PriorityQueue.class);
		QueueImpl queue = new QueueImpl("testqueue", true, pq);
		ServerMessage msg1 = mock(ServerMessage.class);
		ServerMessage msg2 = mock(ServerMessage.class);
		queue.addMessage(msg1);
		queue.addMessage(msg2);
		verify(pq).enqueue(msg1, 0);
		verify(pq).enqueue(msg2, 0);
	}
	
	@Test
	public void shouldAddMessageToQueueWithRightPriority() {
		PriorityQueue<ServerMessage> pq = mock(PriorityQueue.class);
		QueueImpl queue = new QueueImpl("testqueue", true, pq);
		ServerMessage msg = mock(ServerMessage.class);
		when(msg.getPriority()).thenReturn(2);
		queue.addMessage(msg);
		verify(pq).enqueue(msg, 2);
	}

}
