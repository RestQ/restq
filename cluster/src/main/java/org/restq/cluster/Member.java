/**
 * 
 */
package org.restq.cluster;

import java.net.InetSocketAddress;
import java.util.Set;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public interface Member extends DataSerializable {
	
	String getId();

	InetSocketAddress getAddress();
	
	Set<Partition> getPartitions();
	
	void assignPartition(Partition partition);
	
	void unassignPartition(Partition partition);
	
	Set<Member> getSlaves();
	
	void addSlave(Member slave);
	
	void removeSlave(Member slave);
}
