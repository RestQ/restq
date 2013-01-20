/**
 * 
 */
package org.restq.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.cluster.impl.MemberImpl;

/**
 * @author ganeshs
 *
 */
public class MasterInfo extends Response {

	private Member master;
	
	/**
	 * 
	 */
	public MasterInfo() {
	}
	
	/**
	 * @param master
	 */
	public MasterInfo(Member master) {
		this.master = master;
	}

	/**
	 * @return the master
	 */
	public Member getMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(Member master) {
		this.master = master;
	}
	
	@Override
	public void readData(DataInput input) throws IOException {
		super.readData(input);
		master = new MemberImpl();
		master.readData(input);
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		master.writeData(output);
	}

	public String toString() {
		return "MasterInfo [master=" + master + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((master == null) ? 0 : master.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MasterInfo other = (MasterInfo) obj;
		if (master == null) {
			if (other.master != null)
				return false;
		} else if (!master.equals(other.master))
			return false;
		return true;
	}
}
