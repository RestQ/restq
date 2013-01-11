/**
 * 
 */
package org.restq.core.cluster.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.restq.core.cluster.Member;

/**
 * @author ganeshs
 *
 */
public class MemberImpl implements Member {
	
	private String id;
	
	private int index;
	
	private InetSocketAddress address;
	
	/**
	 * 
	 */
	public MemberImpl() {
	}
	
	/**
	 * @param id
	 * @param port
	 * @throws UnknownHostException 
	 */
	public MemberImpl(String id, int port) throws UnknownHostException {
		this(id, new InetSocketAddress(port));
	}
	
	/**
	 * @param id
	 * @param address
	 */
	public MemberImpl(String id, InetSocketAddress address) {
		this.id = id;
		this.address = address;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public InetSocketAddress getAddress() {
		return address;
	}
	
	@Override
	public void readData(DataInput input) throws IOException {
		byte[] bytes = new byte[input.readInt()];
		input.readFully(bytes);
		id = new String(bytes);
		index = input.readInt();
		bytes = new byte[input.readInt()];
		input.readFully(bytes);
		address = new InetSocketAddress(new String(bytes), input.readInt());
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeInt(id.getBytes().length);
		output.write(id.getBytes());
		output.writeInt(index);
		output.writeInt(address.getHostName().getBytes().length);
		output.write(address.getHostName().getBytes());
		output.writeInt(address.getPort());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MemberImpl other = (MemberImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemberImpl [id=" + id + ", index=" + index + ", address=" + address + "]";
	}

}
