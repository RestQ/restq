/**
 * 
 */
package org.restq.core;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
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
	 * @param b
	 * @throws IOException
	 * @see java.io.DataInput#readFully(byte[])
	 */
	public void readFully(byte[] b) throws IOException {
		dataInput.readFully(b);
	}

	/**
	 * @param b
	 * @param off
	 * @param len
	 * @throws IOException
	 * @see java.io.DataInput#readFully(byte[], int, int)
	 */
	public void readFully(byte[] b, int off, int len) throws IOException {
		dataInput.readFully(b, off, len);
	}

	/**
	 * @param n
	 * @return
	 * @throws IOException
	 * @see java.io.DataInput#skipBytes(int)
	 */
	public int skipBytes(int n) throws IOException {
		return dataInput.skipBytes(n);
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
	 * @see java.io.DataInput#readUnsignedByte()
	 */
	public int readUnsignedByte() throws IOException {
		return dataInput.readUnsignedByte();
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
	 * @see java.io.DataInput#readUnsignedShort()
	 */
	public int readUnsignedShort() throws IOException {
		return dataInput.readUnsignedShort();
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
	 * @see java.io.DataInput#readLine()
	 */
	public String readLine() throws IOException {
		return dataInput.readLine();
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
}
