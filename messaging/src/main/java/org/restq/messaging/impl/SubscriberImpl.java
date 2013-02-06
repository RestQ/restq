/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Subscriber;

/**
 * @author ganeshs
 *
 */
public class SubscriberImpl extends ConsumerImpl implements Subscriber {
	
	private String boundDestination;
	
	private String httpUrl;
	
	private String httpMethod;
	
	private boolean durable;
	
	public SubscriberImpl() {
	}
	
	/**
	 * @param id
	 * @param destination
	 */
	public SubscriberImpl(String id, String destination) {
		super(id, destination);
	}

	/**
	 * @param id
	 * @param destination
	 * @param boundDestination
	 * @param httpUrl
	 * @param httpMethod
	 * @param durable
	 */
	public SubscriberImpl(String id, String destination, String boundDestination, String httpUrl, String httpMethod, boolean durable) {
		this(id, destination);
		this.boundDestination = boundDestination;
		this.httpUrl = httpUrl;
		this.httpMethod = httpMethod;
		this.durable = durable;
	}

	public void setBoundDestination(String destination) {
		this.boundDestination = destination;
	}
	
	/**
	 * @return the httpUrl
	 */
	public String getHttpUrl() {
		return httpUrl;
	}

	/**
	 * @param httpUrl the httpUrl to set
	 */
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	/**
	 * @return the httpMethod
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @return the boundDestination
	 */
	public String getBoundDestination() {
		return boundDestination;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
		boundDestination = input.readString();
		httpUrl = input.readString();
		httpMethod = input.readString();
		durable = input.readBoolean();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		output.writeString(boundDestination);
		output.writeString(httpUrl);
		output.writeString(httpMethod);
		output.writeBoolean(durable);
	}

	/**
	 * @return the durable
	 */
	public boolean isDurable() {
		return durable;
	}

	/**
	 * @param durable the durable to set
	 */
	public void setDurable(boolean durable) {
		this.durable = durable;
	}
}
