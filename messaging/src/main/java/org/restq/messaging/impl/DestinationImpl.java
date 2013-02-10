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
	
	private String id;
	
	/**
	 * <p>Default Constructor</p>
	 */
	public DestinationImpl() {
	}
	
	/**
	 * @param id
	 * @param name
	 */
	public DestinationImpl(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getMessageCount() {
		return 0;
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		id = input.readString();
		name = input.readString();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(id);
		output.writeString(name);
	}
}
