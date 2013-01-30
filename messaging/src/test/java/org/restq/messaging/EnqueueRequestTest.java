/**
 * 
 */
package org.restq.messaging;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mockito.InOrder;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class EnqueueRequestTest {
	
	@Test
	public void shouldWriteToDataOutput() throws IOException {
		ServerMessage message = mock(ServerMessage.class);
		EnqueueRequest request = new EnqueueRequest("testqueue", message);
		DataOutputWrapper output = mock(DataOutputWrapper.class);
		request.writeData(output);
		InOrder order = inOrder(output, message);
		order.verify(output).writeString(request.getDestinationId());
		order.verify(message).writeData(output);
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		ServerMessage message = mock(ServerMessage.class);
		EnqueueRequest request = spy(new EnqueueRequest());
		final String destination = "testqueue";
		DataInputWrapper input = mock(DataInputWrapper.class);
		when(input.readString()).thenReturn(destination);
		doReturn(message).when(request).createServerMessage();
		request.readData(input);
		InOrder order = inOrder(input, message);
		order.verify(input).readString();
		order.verify(message).readData(input);
		assertEquals(request.getDestinationId(), destination);
		assertEquals(request.getMessage(), message);
	}
}
