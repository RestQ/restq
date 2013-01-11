/**
 * 
 */
package org.restq.core.cluster;

import java.net.InetSocketAddress;

import org.restq.core.cluster.nio.DataSerializable;

/**
 * @author ganeshs
 *
 */
public interface Member extends DataSerializable {
	
	String getId();

	InetSocketAddress getAddress();
}
