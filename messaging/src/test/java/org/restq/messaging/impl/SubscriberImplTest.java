/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;

import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author ganeshs
 *
 */
public class SubscriberImplTest {

	@Test
	public void shouldWriteToDataOutput() throws IOException {
		SubscriberImpl subscriber = new SubscriberImpl("testId", "testDestination");
		subscriber.setBoundDestination("testBoundDestination");
		subscriber.setHttpMethod("POST");
		subscriber.setHttpUrl("http://localhost:80/topic");
		DataOutputWrapper dataOutput = mock(DataOutputWrapper.class);
		subscriber.writeData(dataOutput);
		InOrder order = inOrder(dataOutput);
		order.verify(dataOutput).writeString(subscriber.getBoundDestination());
		order.verify(dataOutput).writeString(subscriber.getHttpUrl());
		order.verify(dataOutput).writeString(subscriber.getHttpMethod());
		order.verify(dataOutput).writeBoolean(subscriber.isDurable());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		SubscriberImpl subscriber = new SubscriberImpl();
		DataInputWrapper dataInput = mock(DataInputWrapper.class);
		doAnswer(new Answer<String>() {
			private int count;
			
			private String[] values = new String[]{"testId", "testDestination", "testBoundDestination", "http://localhost:80/topics", "POST"};
			
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return values[count++];
			}
		}).when(dataInput).readString();
		when(dataInput.readBoolean()).thenReturn(true);
		subscriber.readData(dataInput);
		assertEquals(subscriber.getBoundDestination(), "testBoundDestination");
		assertEquals(subscriber.getHttpUrl(), "http://localhost:80/topics");
		assertEquals(subscriber.getHttpMethod(), "POST");
		assertTrue(subscriber.isDurable());
	}
}
