/**
 * 
 */
package org.restq.core.cluster.nio;

import org.restq.core.cluster.nio.impl.ConnectionImpl;

/**
 * @author ganeshs
 *
 */
public class ReadHandler {

	private ConnectionImpl connection;
	
	/**
	 * @param connection
	 */
	public ReadHandler(ConnectionImpl connection) {
		this.connection = connection;
	}
}
