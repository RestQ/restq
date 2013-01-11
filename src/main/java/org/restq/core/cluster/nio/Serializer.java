/**
 * 
 */
package org.restq.core.cluster.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class Serializer {
	
	private static Serializer serializer = new Serializer();
	
	public static Serializer instance() {
		return serializer;
	}

	public void serialize(DataOutput output, DataSerializable serializable) throws IOException {
		output.writeUTF(serializable.getClass().getName());
		serializable.writeData(output);
	}
	
	public DataSerializable deserialize(DataInput input) throws IOException {
		String className = input.readUTF();
		DataSerializable serializable = getInstance(className);
		serializable.readData(input);
		return serializable;
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
