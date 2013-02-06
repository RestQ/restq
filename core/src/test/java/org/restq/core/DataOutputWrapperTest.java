/**
 * 
 */
package org.restq.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class DataOutputWrapperTest {

	private DataOutputWrapper wrapper;
	
	private DataOutput dataOutput;
	
	@BeforeMethod
	public void setup() {
		dataOutput = mock(DataOutput.class);
		wrapper = new DataOutputWrapper(dataOutput);
	}
	
	@Test
	public void shouldWriteInt() throws IOException {
		wrapper.write(1);
		verify(dataOutput).write(1);
	}
	
	@Test
	public void shouldWriteByteArray() throws IOException {
		byte[] bytes = "test".getBytes();
		wrapper.write(bytes);
		verify(dataOutput).writeInt(bytes.length);
		verify(dataOutput).write(bytes);
	}
	
	@Test
	public void shouldWriteBoolean() throws IOException {
		wrapper.writeBoolean(true);
		verify(dataOutput).writeBoolean(true);
	}
	
	@Test
	public void shouldWriteByte() throws IOException {
		wrapper.writeByte(2);
		verify(dataOutput).writeByte(2);
	}
	
	@Test
	public void shouldWriteShort() throws IOException {
		wrapper.writeShort(2);
		verify(dataOutput).writeShort(2);
	}
	
	@Test
	public void shouldWriteChar() throws IOException {
		wrapper.writeChar(2);
		verify(dataOutput).writeChar(2);
	}
	
	@Test
	public void shouldWriteInteger() throws IOException {
		wrapper.writeInt(2);
		verify(dataOutput).writeInt(2);
	}
	
	@Test
	public void shouldWriteLong() throws IOException {
		wrapper.writeLong(2);
		verify(dataOutput).writeLong(2);
	}
	
	@Test
	public void shouldWriteFloat() throws IOException {
		wrapper.writeFloat(2);
		verify(dataOutput).writeFloat(2);
	}
	
	@Test
	public void shouldWriteDouble() throws IOException {
		wrapper.writeDouble(2);
		verify(dataOutput).writeDouble(2);
	}
	
	@Test
	public void shouldWriteUTF() throws IOException {
		wrapper.writeUTF("test");
		verify(dataOutput).writeUTF("test");
	}
	
	@Test
	public void shouldWriteString() throws IOException {
		wrapper.writeString("test123");
		verify(dataOutput).writeInt("test123".length());
		verify(dataOutput).write("test123".getBytes());
	}
	
	@Test
	public void shouldWriteNull() throws IOException {
		wrapper.writeString(null);
		verify(dataOutput).writeInt(0);
	}
	
	@Test
	public void shouldWriteMap() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "value1");
		map.put("key12", "value12");
		wrapper.writeMap(map);
		verify(dataOutput).writeInt(map.size());
		for (Entry<String, String> entry : map.entrySet()) {
			verify(dataOutput).writeInt(entry.getKey().length());
			verify(dataOutput).write(entry.getKey().getBytes());
			verify(dataOutput).writeInt(entry.getValue().length());
			verify(dataOutput).write(entry.getValue().getBytes());
		}
	}
	
	@Test
	public void shouldWriteList() throws IOException {
		List<DataSerializable> list = new ArrayList<DataSerializable>();
		list.add(mock(DataSerializable.class));
		list.add(mock(DataSerializable.class));
		wrapper.writeList(list);
		verify(dataOutput).writeInt(list.size());
		for (DataSerializable serializable : list) {
			verify(serializable).writeData(wrapper);
		}
	}
}
