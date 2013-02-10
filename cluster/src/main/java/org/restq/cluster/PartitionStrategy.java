/**
 * 
 */
package org.restq.cluster;

import java.io.Serializable;

import org.restq.core.Identifiable;

/**
 * @author ganeshs
 *
 */
public interface PartitionStrategy {

	Partition getPartition(Serializable... key);
	
	Partition getPartition(Identifiable... key);
}
