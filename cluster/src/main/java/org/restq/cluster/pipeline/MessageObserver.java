/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.cluster.Request;
import org.restq.cluster.Response;

/**
 * @author ganeshs
 *
 */
public interface MessageObserver {

	void onReceived(Request request);
	
	void onSuccess(Request request, Response response);
	
	void onFailure(Request request, Response response, Throwable throwable);
	
	void onCompleted(Request request, Response response);
}
