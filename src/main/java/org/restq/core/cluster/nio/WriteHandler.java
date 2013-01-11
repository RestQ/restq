/**
 * 
 */
package org.restq.core.cluster.nio;

import org.restq.core.cluster.nio.impl.ConnectionImpl;

/**
 * @author ganeshs
 *
 */
public class WriteHandler {
	
	private ConnectionImpl connection;

	public WriteHandler(ConnectionImpl connection) {
		this.connection = connection;
	}
}
