/**
 * 
 */
package org.restq.cluster.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.DataSerializable;
import org.restq.core.RestQException;
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
