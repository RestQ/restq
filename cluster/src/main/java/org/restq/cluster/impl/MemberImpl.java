/**
 * 
 */
package org.restq.cluster.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.restq.cluster.Member;
import org.restq.cluster.Partition;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;

/**
 * @author ganeshs
 *
 */
public class MemberImpl implements Member {
	
	private String id;
	
	private Set<Partition> partitions = new HashSet<Partition>();
	
	private InetSocketAddress address;
	
	private Set<Member> slaves = new HashSet<Member>();
	
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
	public MemberImpl(String id, int port) {
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
	public void readData(DataInputWrapper input) throws IOException {
		id = input.readString();
		int partitionCount = input.readInt();
		for (int i = 0; i < partitionCount; i++) {
			Partition partition = new Partition();
			partition.readData(input);
			partitions.add(partition);
		}
		address = new InetSocketAddress(input.readString(), input.readInt());
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(id);
		output.writeInt(partitions.size());
		for (Partition partition : partitions) {
			partition.writeData(output);
		}
		output.writeString(address.getHostName());
		output.writeInt(address.getPort());
	}

	/**
	 * @return the partitions
	 */
	public Set<Partition> getPartitions() {
		return Collections.unmodifiableSet(partitions);
	}
	
	@Override
	public void assignPartition(Partition partition) {
		this.partitions.add(partition);
	}
	
	@Override
	public void unassignPartition(Partition partition) {
		this.partitions.remove(partition);
	}
	
	@Override
	public void addSlave(Member slave) {
		this.slaves.add(slave);
	}
	
	@Override
	public void removeSlave(Member slave) {
		this.slaves.remove(slave);
	}
	
	@Override
	public Set<Member> getSlaves() {
		return slaves;
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
		return "MemberImpl [id=" + id + ", partitions=" + partitions + ", address=" + address + ", slaveCount=" + slaves.size() + "]";
	}

}
