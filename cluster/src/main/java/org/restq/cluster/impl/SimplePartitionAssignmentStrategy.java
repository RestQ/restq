/**
 * 
 */
package org.restq.cluster.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.Partition;
import org.restq.cluster.PartitionAssignmentStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class SimplePartitionAssignmentStrategy implements PartitionAssignmentStrategy {
	
	@Autowired
	private Cluster cluster;
	
	public SimplePartitionAssignmentStrategy() {
	}
	
	public SimplePartitionAssignmentStrategy(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public void strategize(Member member, boolean join) {
		member.assignPartition(nextPartition());
	}

	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	protected Partition nextPartition() {
		int partitionCount = 0;
		for (Member member : cluster.getMembers()) {
			partitionCount += member.getPartitions().size();
		}
		return new Partition(partitionCount + 1);
	}
}
