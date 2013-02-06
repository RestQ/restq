/**
 * 
 */
package org.restq.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ganeshs
 *
 */
public class DataOutputWrapper {

	private DataOutput dataOutput;
	
	public DataOutputWrapper(DataOutput dataOutput) {
		this.dataOutput = dataOutput;
	}

	/**
	 * @return the dataOutput
	 */
	public DataOutput getDataOutput() {
		return dataOutput;
	}

	/**
	 * @param b
	 * @throws IOException
	 * @see java.io.DataOutput#write(int)
	 */
	public void write(int b) throws IOException {
		dataOutput.write(b);
	}

	/**
	 * @param b
	 * @throws IOException
	 * @see java.io.DataOutput#write(byte[])
	 */
	public void write(byte[] b) throws IOException {
		dataOutput.writeInt(b.length);
		dataOutput.write(b);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeBoolean(boolean)
	 */
	public void writeBoolean(boolean v) throws IOException {
		dataOutput.writeBoolean(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeByte(int)
	 */
	public void writeByte(int v) throws IOException {
		dataOutput.writeByte(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeShort(int)
	 */
	public void writeShort(int v) throws IOException {
		dataOutput.writeShort(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeChar(int)
	 */
	public void writeChar(int v) throws IOException {
		dataOutput.writeChar(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeInt(int)
	 */
	public void writeInt(int v) throws IOException {
		dataOutput.writeInt(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeLong(long)
	 */
	public void writeLong(long v) throws IOException {
		dataOutput.writeLong(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeFloat(float)
	 */
	public void writeFloat(float v) throws IOException {
		dataOutput.writeFloat(v);
	}

	/**
	 * @param v
	 * @throws IOException
	 * @see java.io.DataOutput#writeDouble(double)
	 */
	public void writeDouble(double v) throws IOException {
		dataOutput.writeDouble(v);
	}

	/**
	 * @param s
	 * @throws IOException
	 * @see java.io.DataOutput#writeUTF(java.lang.String)
	 */
	public void writeUTF(String s) throws IOException {
		dataOutput.writeUTF(s);
	}
	
	public void writeString(String data) throws IOException {
		if (data != null) {
			dataOutput.writeInt(data.length());
			dataOutput.write(data.getBytes());
		} else {
			dataOutput.writeInt(0);
		}
	}
	
	public void writeMap(Map<String, String> map) throws IOException {
		dataOutput.writeInt(map.size());
		for (Entry<String, String> entry : map.entrySet()) {
			writeString(entry.getKey());
			writeString(entry.getValue());
		}
	}
	
	public <T extends DataSerializable> void writeList(List<T> list) throws IOException {
		dataOutput.writeInt(list.size());
		for (DataSerializable serializable : list) {
			serializable.writeData(this);
		}
	}
	
}
