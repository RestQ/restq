/**
 * 
 */
package org.restq.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.DataSerializable;

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
	 * @param message
	 */
	public Response(Status status, String message) {
		this.status = status;
		this.message = message;
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeInt(status.ordinal());
		if (message != null) {
			output.writeInt(message.getBytes().length);
			output.write(message.getBytes());
		} else {
			output.writeInt(0);
		}
		
	}

	@Override
	public void readData(DataInput input) throws IOException {
		setStatus(Status.values()[input.readInt()]);
		byte[] bytes = new byte[input.readInt()];
		input.readFully(bytes);
		setMessage(new String(bytes));
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
