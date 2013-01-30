/**
 * 
 */
package org.restq.messaging.impl;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mockito.InOrder;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.ServerMessage;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class TopicImplTest {

	@Test
	public void shouldWriteToDataOutput() throws IOException {
		TopicImpl topic = new TopicImpl("testtopic");
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
		when(input.readString()).thenReturn(name);
		topic.readData(input);
		InOrder order = inOrder(input);
		order.verify(input).readString();
		assertEquals(topic.getName(), name);
	}
	
	@Test
	public void shouldAddMessageToQueue() {
		TopicImpl topic = new TopicImpl("testtopic");
		ServerMessage msg = mock(ServerMessage.class);
		topic.addMessage(msg);
	}
}
