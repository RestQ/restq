/**
 * 
 */
package org.restq.core;

import java.io.IOException;



/**
 * @author ganeshs
 *
 */
public class Response implements DataSerializable {
	
	public enum Status {
		success, failed
	}
	
	private Status status = Status.success;
	
	private String message;
	
	public Response() {
	}
	
	/**
	 * @param status
	 */
	public Response(Status status) {
		this.status = status;
	}
	
	/**
	 * @param status
	 * @param message
	 */
	public Response(Status status, String message) {
		this(status);
		this.message = message;
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeInt(status.ordinal());
		output.writeString(message);
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		setStatus(Status.values()[input.readInt()]);
		setMessage(input.readString());
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
