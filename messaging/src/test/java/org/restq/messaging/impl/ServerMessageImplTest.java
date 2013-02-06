/**
 * 
 */
package org.restq.messaging.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.restq.messaging.Filter;
import org.restq.messaging.Filter.Operator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author ganeshs
 *
 */
public class ServerMessageImplTest {
	
	private ServerMessageImpl message;
	
	@BeforeMethod
	public void setup() {
		message = new ServerMessageImpl();
		message.addProperty("testa", "testa123");
		message.addProperty("testb", "testb123");
	}

	@Test
	public void shouldMatchOnlyIfAllFiltersMatch() {
		Filter filter1 = new Filter("testa", "testa123", Operator.eq);
		Filter filter2 = new Filter("testb", "testb123", Operator.contains);
		assertTrue(message.matches(Arrays.asList(filter1, filter2)));
	}
	
	@Test
	public void shouldNotMatchIfOneOfTheFiltersDoesntMatch() {
		Filter filter1 = new Filter("testa", "testa123", Operator.eq);
		Filter filter2 = new Filter("testb", "testa123", Operator.contains);
		assertFalse(message.matches(Arrays.asList(filter1, filter2)));
	}
}
