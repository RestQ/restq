/**
 * 
 */
package org.restq.core;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ganeshs
 *
 */
public class DataInputWrapper {

	private DataInput dataInput;
	
	public DataInputWrapper(DataInput dataInput) {
		this.dataInput = dataInput;
	}

	/**
	 * @return the dataInput
	 */
	public DataInput getDataInput() {
		return dataInput;
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readBoolean()
	 */
	public boolean readBoolean() throws IOException {
		return dataInput.readBoolean();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readByte()
	 */
	public byte readByte() throws IOException {
		return dataInput.readByte();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readShort()
	 */
	public short readShort() throws IOException {
		return dataInput.readShort();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readChar()
	 */
	public char readChar() throws IOException {
		return dataInput.readChar();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readInt()
	 */
	public int readInt() throws IOException {
		return dataInput.readInt();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readLong()
	 */
	public long readLong() throws IOException {
		return dataInput.readLong();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readFloat()
	 */
	public float readFloat() throws IOException {
		return dataInput.readFloat();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readDouble()
	 */
	public double readDouble() throws IOException {
		return dataInput.readDouble();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#readUTF()
	 */
	public String readUTF() throws IOException {
		return dataInput.readUTF();
	}
	
	public String readString() throws IOException {
		byte[] bytes = new byte[dataInput.readInt()];
		dataInput.readFully(bytes);
		return new String(bytes);
	}
	
	public byte[] read() throws IOException {
		byte[] bytes = new byte[dataInput.readInt()];
		dataInput.readFully(bytes);
		return bytes;
	}
	
	public Map<String, String> readMap() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < dataInput.readInt(); i++) {
			map.put(readString(), readString());
		}
		return map;
	}
	
	public <T extends DataSerializable> List<T> readList(DataSerializableBuilder<T> builder) throws IOException {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < dataInput.readInt(); i++) {
			T t = builder.newInstance();
			t.readData(this);
			list.add(t);
		}
		return list;
	}
	
	/**
	 * @author ganeshs
	 *
	 * @param <T>
	 */
	public static interface DataSerializableBuilder<T extends DataSerializable> {
		
		T newInstance();
	}
}
