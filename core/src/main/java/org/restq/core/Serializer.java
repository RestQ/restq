/**
 * 
 */
package org.restq.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;


/**
 * @author ganeshs
 *
 */
@Service
public class Serializer {
	
	public void serialize(DataOutput output, DataSerializable serializable) throws IOException {
		output.writeUTF(serializable.getClass().getName());
		serializable.writeData(output);
	}
	
	public byte[] serialize(DataSerializable serializable) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutput output = new DataOutputStream(os);
		serialize(output, serializable);
		return os.toByteArray();
	}
	
	public DataSerializable deserialize(DataInput input) throws IOException {
		String className = input.readUTF();
		DataSerializable serializable = getInstance(className);
		serializable.readData(input);
		return serializable;
	}
	
	public DataSerializable deserialize(byte[] data) throws IOException {
		DataInput input = new DataInputStream(new ByteArrayInputStream(data));
		return deserialize(input);
	}
	
	public DataSerializable getInstance(String className) {
		DataSerializable serializable = null;
		try {
			serializable = (DataSerializable) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RestQException(e);
		}
		return serializable;
	}
}
