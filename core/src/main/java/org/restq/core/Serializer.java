/**
 * 
 */
package org.restq.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;


/**
 * @author ganeshs
 *
 */
@Service
public class Serializer {
	
	public void serialize(DataOutputWrapper output, DataSerializable serializable) throws IOException {
		output.writeUTF(serializable.getClass().getName());
		serializable.writeData(output);
	}
	
	public byte[] serialize(DataSerializable serializable) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutputWrapper output = new DataOutputWrapper(new DataOutputStream(os));
		serialize(output, serializable);
		return os.toByteArray();
	}
	
	public DataSerializable deserialize(DataInputWrapper input) throws IOException {
		String className = input.readUTF();
		DataSerializable serializable = getInstance(className);
		serializable.readData(input);
		return serializable;
	}
	
	public DataSerializable deserialize(byte[] data) throws IOException {
		DataInputWrapper input = new DataInputWrapper(new DataInputStream(new ByteArrayInputStream(data)));
		return deserialize(input);
	}
	
	protected DataSerializable getInstance(String className) {
		DataSerializable serializable = null;
		try {
			serializable = (DataSerializable) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RestQException(e);
		}
		return serializable;
	}
}
