/**
 * 
 */
package org.restq.messaging.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.messaging.Destination;

/**
 * @author ganeshs
 *
 */
public abstract class DestinationImpl implements Destination {
	
	private String name;
	
	/**
	 * <p>Default Constructor</p>
	 */
	public DestinationImpl() {
	}
	
	/**
	 * @param name
	 */
	public DestinationImpl(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getMessageCount() {
		return 0;
	}

	@Override
	public void readData(DataInput input) throws IOException {
		int length = input.readInt();
		byte[] bytes = new byte[length];
		input.readFully(bytes);
		name = new String(bytes);
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		int length = 0;
		if (name != null) {
			length = name.length();
		}
		output.writeInt(length);
		if (name != null) {
			output.write(name.getBytes());
		}
	}
}
