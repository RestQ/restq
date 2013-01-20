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
public class MessageContext {

	private Request request;
	
	private Response response;
	
	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
}
