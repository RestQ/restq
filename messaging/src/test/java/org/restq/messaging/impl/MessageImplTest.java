/**
 * 
 */
package org.restq.messaging.impl;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mockito.InOrder;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class MessageImplTest {

	@Test
	public void shouldWriteToDataOutput() throws IOException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("key1", "value1");
		MessageImpl message = new MessageImpl("1234", "body".getBytes(), properties);
		DataOutputWrapper output = mock(DataOutputWrapper.class);
		message.writeData(output);
		InOrder order = inOrder(output);
		order.verify(output).writeString(message.getId());
		order.verify(output).write(message.getBody());
		order.verify(output).writeMap(message.getProperties());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("key1", "value1");
		String id = "test123";
		byte[] body = "body".getBytes();
		MessageImpl message = new MessageImpl();
		DataInputWrapper input = mock(DataInputWrapper.class);
		when(input.readString()).thenReturn(id);
		when(input.read()).thenReturn(body);
		when(input.readMap()).thenReturn(properties);
		message.readData(input);
		InOrder order = inOrder(input);
		order.verify(input).readString();
		order.verify(input).read();
		order.verify(input).readMap();
		assertEquals(message.getId(), id);
		assertEquals(message.getBody(), body);
		assertEquals(message.getProperties(), properties);
	}
	
	@Test
	public void shouldAddProperty() {
		MessageImpl message = new MessageImpl();
		message.addProperty("key", "value");
		assertEquals(message.getProperty("key"), "value");
	}
}
