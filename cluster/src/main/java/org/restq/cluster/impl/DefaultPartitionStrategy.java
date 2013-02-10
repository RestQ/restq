/**
 * 
 */
package org.restq.cluster.impl;

import java.io.Serializable;

import org.restq.cluster.Cluster;
import org.restq.cluster.Partition;
import org.restq.cluster.PartitionStrategy;
import org.restq.core.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author ganeshs
 *
 */
public class DefaultPartitionStrategy implements PartitionStrategy {
	
	@Autowired
	private Cluster cluster;

	@Override
	public Partition getPartition(Serializable... keys) {
		int partitionCount = cluster.getPartitions();
		int hash = StringUtils.arrayToDelimitedString(keys, "_").hashCode();
		return new Partition((Math.abs(hash) % partitionCount) + 1);
	}
	
	@Override
	public Partition getPartition(Identifiable... keys) {
		Serializable[] serializables = new Serializable[keys.length];
		for (int i = 0; i < keys.length; i++) {
			serializables[i] = keys[i].getId();
		}
		return getPartition(serializables);
	}
	
}
