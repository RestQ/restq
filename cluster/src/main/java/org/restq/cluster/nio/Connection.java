/**
 * 
 */
package org.restq.cluster.nio;

import java.net.InetSocketAddress;

import org.restq.cluster.Request;

/**
 * @author ganeshs
 *
 */
public interface Connection {

	void open();
	
	void close();
	
	boolean isOpen();
	
	InetSocketAddress getAddress();
	
	ResponseFuture send(Request request);
	
}
