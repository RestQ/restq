/**
 * 
 */
package org.restq.core.cluster.nio;

import java.net.InetSocketAddress;

import org.restq.core.cluster.Request;

/**
 * @author ganeshs
 *
 */
public interface Connection {

	void open();
	
	void close();
	
	InetSocketAddress getAddress();
	
	void send(Request request);
	
}
