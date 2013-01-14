/**
 * 
 */
package org.restq.core.cluster.nio;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.DummyException;
import org.restq.core.cluster.nio.DataSerializable;
import org.restq.core.cluster.nio.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author ganeshs
 *
 */
public class SerializerTest {
	
	private DataOutput dataOutput;
	
	private DataInput dataInput;
	
	private Serializer serializer;

	@BeforeMethod
	public void setup() {
		dataOutput = mock(DataOutput.class);
		dataInput = mock(DataInput.class);
		serializer = spy(new Serializer());
	}
	
	@Test(expectedExceptions=DummyException.class)
	public void testSerializeWritesClassName() throws IOException {
		DataSerializable serializable = mock(DataSerializable.class);
		doThrow(new DummyException()).when(dataOutput).writeUTF(serializable.getClass().getName());
		serializer.serialize(dataOutput, serializable);
	}
	
	@Test(expectedExceptions=DummyException.class)
	public void testSerializeWritesData() throws IOException {
		DataSerializable serializable = mock(DataSerializable.class);
		doNothing().when(dataOutput).writeUTF(serializable.getClass().getName());
		doThrow(new DummyException()).when(serializable).writeData(dataOutput);
		serializer.serialize(dataOutput, serializable);
	}
	
	@Test(expectedExceptions=DummyException.class)
	public void testDeserialize() throws IOException {
		DataSerializable serializable = mock(DataSerializable.class);
		doReturn(serializable).when(serializer).getInstance(DataSerializable.class.getName());
		when(dataInput.readUTF()).thenReturn(DataSerializable.class.getName());
		doThrow(new DummyException()).when(serializable).readData(dataInput);
		serializer.deserialize(dataInput);
	}
	
}
