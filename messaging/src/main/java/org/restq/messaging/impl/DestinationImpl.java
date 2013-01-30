/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
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
	public void readData(DataInputWrapper input) throws IOException {
		name = input.readString();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(name);
	}
}
