/**
 * 
 */
package org.restq.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class Partition implements DataSerializable, Comparable<Partition> {

	private int index;
	
	public Partition() {
	}
	
	/**
	 * @param index
	 */
	public Partition(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	@Override
	public void readData(DataInput input) throws IOException {
		index = input.readInt();
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeInt(index);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Partition other = (Partition) obj;
		if (index != other.index)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Partition [index=" + index + "]";
	}
	
	@Override
	public int compareTo(Partition o) {
		if (index < o.index) {
			return  -1;
		}
		if (index == o.index) {
			return 0;
		}
		return 1;
	}
}
