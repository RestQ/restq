/**
 * 
 */
package org.restq.messaging;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.Request;
import org.restq.messaging.impl.ServerMessageImpl;

/**
 * @author ganeshs
 *
 */
public class EnqueueRequest extends Request {

	private String destinationId;
	
	private ServerMessage message;
	
	public EnqueueRequest() {
	}

	/**
	 * @param destinationId
	 * @param message
	 */
	public EnqueueRequest(String destinationId, ServerMessage message) {
		this.destinationId = destinationId;
		this.message = message;
	}

	/**
	 * @return the destinationId
	 */
	public String getDestinationId() {
		return destinationId;
	}

	/**
	 * @param destinationId the destinationId to set
	 */
	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	/**
	 * @return the message
	 */
	public ServerMessage getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(ServerMessage message) {
		this.message = message;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
		destinationId = input.readString();
		message = createServerMessage();
		message.readData(input);
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		output.writeString(destinationId);
		message.writeData(output);
	}
	
	protected ServerMessage createServerMessage() {
		return new ServerMessageImpl();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destinationId == null) ? 0 : destinationId.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnqueueRequest other = (EnqueueRequest) obj;
		if (destinationId == null) {
			if (other.destinationId != null)
				return false;
		} else if (!destinationId.equals(other.destinationId))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
}