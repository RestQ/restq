/**
 * 
 */
package org.restq.journal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

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
	public void readData(DataInput input) throws IOException {
		id = input.readLong();
		type = input.readByte();
		int length = input.readInt();
		if (length > 0) {
			data = new byte[length];
			input.readFully(data);
		}
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeLong(id);
		output.writeByte(type);
		if (data == null) {
			output.writeInt(0);
		} else {
			output.writeInt(data.length);
			output.write(data);
		}
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
