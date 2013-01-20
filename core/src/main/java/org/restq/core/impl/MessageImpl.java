/**
 * 
 */
package org.restq.core.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.restq.core.Message;

/**
 * @author ganeshs
 *
 */
public class MessageImpl implements Message {
	
	private Serializable id;
	
	private byte[] body;
	
	private int priority = 5;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	/**
	 * <p>Default constructor</p>
	 */
	public MessageImpl() {
	}

	/**
	 * @param id
	 * @param body
	 */
	public MessageImpl(Serializable id, byte[] body) {
		this.id = id;
		this.body = body;
	}

	@Override
	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	@Override
	public void getProperty(String name) {
		properties.get(name);
	}

	/**
	 * @return the id
	 */
	public Serializable getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Serializable id) {
		this.id = id;
	}

	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public void readData(DataInput input) throws IOException {
		int length = input.readInt();
		body = new byte[length];
		input.readFully(body);
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeInt(body.length);
		output.write(body);
	}
}
