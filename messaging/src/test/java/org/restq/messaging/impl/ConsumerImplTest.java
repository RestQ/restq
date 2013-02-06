/**
 * 
 */
package org.restq.messaging.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataInputWrapper.DataSerializableBuilder;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Filter;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class ConsumerImplTest {

	@Test
	public void shouldAddFilter() {
		ConsumerImpl consumer = new ConsumerImpl("testId", "testDestination");
		Filter filter = new Filter("name", "value");
		consumer.addFilter(filter);
		assertTrue(consumer.getFilters().contains(filter));
	}
	
	@Test
	public void shouldGetFilters() {
		ConsumerImpl consumer = new ConsumerImpl("testId", "testDestination");
		consumer.addFilter(new Filter("name", "value"));
		consumer.addFilter(new Filter("name1", "value1"));
		assertEquals(consumer.getFilters().size(), 2);
	}
	
	@Test
	public void shouldWriteToDataOutput() throws IOException {
		ConsumerImpl consumer = new ConsumerImpl("testId", "testDestination");
		consumer.addFilter(new Filter("name", "value"));
		consumer.addFilter(new Filter("name1", "value1"));
		DataOutputWrapper dataOutput = mock(DataOutputWrapper.class);
		consumer.writeData(dataOutput);
		InOrder order = inOrder(dataOutput);
		order.verify(dataOutput).writeString(consumer.getId());
		order.verify(dataOutput).writeString(consumer.getDestination());
		order.verify(dataOutput).writeList(consumer.getFilters());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		ConsumerImpl consumer = new ConsumerImpl();
		DataInputWrapper dataInput = mock(DataInputWrapper.class);
		doAnswer(new Answer<String>() {
			private int count;
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return count ++ == 0 ? "testId" : "testDestination";
			}
		}).when(dataInput).readString();
		when(dataInput.readList(any(DataSerializableBuilder.class))).thenReturn(Arrays.asList(mock(Filter.class), mock(Filter.class)));
		consumer.readData(dataInput);
		assertEquals(consumer.getId(), "testId");
		assertEquals(consumer.getDestination(), "testDestination");
		assertEquals(consumer.getFilters().size(), 2);
	}
}
