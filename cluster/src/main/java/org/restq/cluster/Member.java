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
}
