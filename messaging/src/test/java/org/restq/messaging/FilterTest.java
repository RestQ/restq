/**
 * 
 */
package org.restq.messaging;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Filter.Operator;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class FilterTest {
	
	@Test
	public void shouldDefaultToEqWhenOperatorIsNotSet() {
		Filter filter = new Filter("test", "test123");
		assertEquals(filter.getOperator(), Operator.eq);
	}

	@Test
	public void shouldMatchEqualityOfTheValue() {
		Filter filter = new Filter("test", "test123", Operator.eq);
		assertTrue(filter.matches("test123"));
	}
	
	@Test
	public void shouldNotMatchEqualityOfTheValue() {
		Filter filter = new Filter("test", "test123", Operator.eq);
		assertFalse(filter.matches("junk"));
	}
	
	@Test
	public void shouldMatchWhenValueContainsGivenString() {
		Filter filter = new Filter("test", "test123", Operator.contains);
		assertTrue(filter.matches("abctest123cs"));
	}
	
	@Test
	public void shouldNotMatchWhenValueDoesntContainsGivenString() {
		Filter filter = new Filter("test", "test123", Operator.contains);
		assertFalse(filter.matches("junktest12"));
	}
	
	@Test
	public void shouldWriteToDataOutput() throws IOException {
		Filter filter = new Filter("test", "test123", Operator.contains);
		DataOutputWrapper output = mock(DataOutputWrapper.class);
		filter.writeData(output);
		InOrder order = inOrder(output);
		order.verify(output).writeString(filter.getName());
		order.verify(output).writeString(filter.getValue());
		order.verify(output).writeInt(filter.getOperator().ordinal());
	}
	
	@Test
	public void shouldReadFromDataInput() throws IOException {
		Filter filter = new Filter();
		DataInputWrapper input = mock(DataInputWrapper.class);
		final AtomicInteger integer = new AtomicInteger(0);
		when(input.readInt()).thenReturn(Operator.contains.ordinal());
		doAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				if (integer.incrementAndGet() == 1) {
					return "name";
				} else {
					return "value";
				}
			}
		}).when(input).readString();
		filter.readData(input);
		InOrder order = inOrder(input);
		order.verify(input, times(2)).readString();
		order.verify(input).readInt();
		assertEquals(filter.getName(), "name");
		assertEquals(filter.getValue(), "value");
		assertEquals(filter.getOperator(), Operator.contains);
	}
}
