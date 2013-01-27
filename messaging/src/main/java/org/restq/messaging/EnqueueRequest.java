/**
 * 
 */
package org.restq.messaging;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.Request;

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
	public void readData(DataInput input) throws IOException {
		super.readData(input);
		
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		output.writeInt(destinationId.length());
		output.write(destinationId.getBytes());
		message.writeData(output);
	}
}