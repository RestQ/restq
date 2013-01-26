/**
 * 
 */
package org.restq.cluster.nio;

import org.restq.core.Response;

/**
 * @author ganeshs
 *
 */
public interface ResponseFuture {

	boolean isDone();

	Response get() throws InterruptedException;
	
	void addListener(ResponseFutureListener listener);
	
}
