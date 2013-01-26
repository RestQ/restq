/**
 * 
 */
package org.restq.cluster.nio.impl;

import java.util.ArrayList;
import java.util.List;

import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.nio.ResponseFutureListener;
import org.restq.core.Response;

/**
 * @author ganeshs
 *
 */
public class ResponseFutureImpl implements ResponseFuture {
	
	private List<ResponseFutureListener> listeners = new ArrayList<ResponseFutureListener>();
	
	private Response response;
	
	private Object monitor = new Object();

	@Override
	public boolean isDone() {
		return response != null;
	}

	@Override
	public Response get() throws InterruptedException {
		synchronized (monitor) {
			if (response == null) {
				monitor.wait();
			}
		}
		return response;
	}

	@Override
	public void addListener(ResponseFutureListener listener) {
		synchronized (monitor) {
			listeners.add(listener);
		}
	}
	
	public void set(Response response) {
		synchronized (monitor) {
			this.response = response;
			monitor.notifyAll();
		}
		for (ResponseFutureListener listener : listeners) {
			listener.completed(this);
		}
	}
}
