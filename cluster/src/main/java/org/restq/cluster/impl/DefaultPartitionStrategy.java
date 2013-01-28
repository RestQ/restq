/**
 * 
 */
package org.restq.cluster.impl;

import java.io.Serializable;

import org.restq.cluster.Cluster;
import org.restq.cluster.Partition;
import org.restq.cluster.PartitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class DefaultPartitionStrategy implements PartitionStrategy {
	
	@Autowired
	private Cluster cluster;

	@Override
	public Partition getPartition(Serializable key) {
		int partitionCount = cluster.getPartitions();
		return new Partition((Math.abs(key.hashCode()) % partitionCount) + 1);
	}

}
