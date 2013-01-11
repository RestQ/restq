/**
 * 
 */
package org.restq.core.server.impl;

import org.restq.core.server.Destination;

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

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public long getMessageCount() {
		return 0;
	}

}
