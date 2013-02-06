/**
 * 
 */
package org.restq.core;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.DataInputWrapper.DataSerializableBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class DataInputWrapperTest {

	private DataInputWrapper wrapper;
	
	private DataInput dataInput;
	
	@BeforeMethod
	public void setup() {
		dataInput = mock(DataInput.class);
		wrapper = new DataInputWrapper(dataInput);
	}
	
	@Test
	public void shouldReadBoolean() throws IOException {
		when(dataInput.readBoolean()).thenReturn(true);
		assertTrue(wrapper.readBoolean());
	}
	
	@Test
	public void shouldReadByte() throws IOException {
		when(dataInput.readByte()).thenReturn((byte)2);
		assertEquals(wrapper.readByte(), (byte)2);
	}
	
	@Test
	public void shouldReadShort() throws IOException {
		when(dataInput.readShort()).thenReturn((short)2);
		assertEquals(wrapper.readShort(), (short)2);
	}
	
	@Test
	public void shouldReadLong() throws IOException {
		when(dataInput.readLong()).thenReturn((long)2);
		assertEquals(wrapper.readLong(), (long)2);
	}
	
	@Test
	public void shouldReadFloat() throws IOException {
		when(dataInput.readFloat()).thenReturn((float)2);
		assertEquals(wrapper.readFloat(), (float)2);
	}
	
	@Test
	public void shouldReadDouble() throws IOException {
		when(dataInput.readDouble()).thenReturn((double)2);
		assertEquals(wrapper.readDouble(), (double)2);
	}
	
	@Test
	public void shouldReadInteger() throws IOException {
		when(dataInput.readInt()).thenReturn(2);
		assertEquals(wrapper.readInt(), 2);
	}
	
	@Test
	public void shouldReadChar() throws IOException {
		when(dataInput.readChar()).thenReturn((char)2);
		assertEquals(wrapper.readChar(), (char)2);
	}
	
	@Test
	public void shouldReadUTF() throws IOException {
		when(dataInput.readUTF()).thenReturn("test123");
		assertEquals(wrapper.readUTF(), "test123");
	}
	
	@Test
	public void shouldReadString() throws IOException {
		final String string = "test123";
		when(dataInput.readInt()).thenReturn(string.length());
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				byte[] bytes = (byte[]) invocation.getArguments()[0];
				System.arraycopy(string.getBytes(), 0, bytes, 0, string.length());
				return null;
			}
		}).when(dataInput).readFully((byte[])anyObject());
		assertEquals(wrapper.readString(), "test123");
	}
	
	@Test
	public void shouldReadBytes() throws IOException {
		final byte[] bytes = "test123".getBytes();
		when(dataInput.readInt()).thenReturn(bytes.length);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				byte[] localBytes = (byte[]) invocation.getArguments()[0];
				System.arraycopy(bytes, 0, localBytes, 0, bytes.length);
				return null;
			}
		}).when(dataInput).readFully((byte[])anyObject());
		assertEquals(wrapper.read(), bytes);
	}
	
	@Test
	public void shouldReadMap() throws IOException {
		wrapper = spy(wrapper);
		when(dataInput.readInt()).thenReturn(2);
		final AtomicInteger integer = new AtomicInteger();
		doAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "string" + integer.incrementAndGet();
			}
		}).when(wrapper).readString();
		Map<String, String> map = wrapper.readMap();
		assertEquals(map.size(), 2);
		assertEquals(map.get("string1"), "string2");
		assertEquals(map.get("string3"), "string4");
	}
	
	@Test
	public void shouldReadList() throws IOException {
		DataSerializableBuilder<DataSerializable> builder = mock(DataSerializableBuilder.class);
		final DataSerializable s1 = mock(DataSerializable.class);
		final DataSerializable s2 = mock(DataSerializable.class);
		when(dataInput.readInt()).thenReturn(2);
		final AtomicInteger integer = new AtomicInteger();
		doAnswer(new Answer<DataSerializable>() {
			@Override
			public DataSerializable answer(InvocationOnMock invocation) throws Throwable {
				return integer.incrementAndGet() == 1 ? s1 : s2;
			}
		}).when(builder).newInstance();
		List<DataSerializable> list = wrapper.readList(builder);
		verify(s1).readData(wrapper);
		verify(s2).readData(wrapper);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), s1);
		assertEquals(list.get(1), s2);
	}
}
