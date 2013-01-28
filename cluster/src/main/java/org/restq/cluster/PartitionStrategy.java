/**
 * 
 */
package org.restq.cluster;

import java.io.Serializable;

/**
 * @author ganeshs
 *
 */
public interface PartitionStrategy {

	Partition getPartition(Serializable key);
}
