/**
 * 
 */
package org.restq.journal;

import java.io.IOException;
import java.util.Arrays;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class Record implements DataSerializable {

	private long id;
	
	private byte type;
	
	private byte[] data;
	
	public Record() {
	}

	/**
	 * @param id
	 * @param type
	 * @param data
	 */
	public Record(long id, byte type, byte[] data) {
		this.id = id;
		this.type = type;
		this.data = data;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		id = input.readLong();
		type = input.readByte();
		data = input.read();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeLong(id);
		output.writeByte(type);
		output.write(data);
	}
	
	public long size() {
		long length = 13; //  8 (long id) + 1 (byte type) + 4 (int length)
		if (data != null) {
			length += data.length;
		}
		return length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Record [id=" + id + ", type=" + type + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + type;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
