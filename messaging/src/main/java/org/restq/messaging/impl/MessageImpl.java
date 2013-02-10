/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Message;

/**
 * @author ganeshs
 *
 */
public class MessageImpl implements Message {
	
	private String id;
	
	private byte[] body;
	
	private int priority = 5;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	public static final String GROUP_ID = "groupId";
	
	/**
	 * <p>Default constructor</p>
	 */
	public MessageImpl() {
	}

	/**
	 * @param id
	 * @param body
	 */
	public MessageImpl(String id, byte[] body) {
		this.id = id;
		this.body = body;
	}
	
	/**
	 * @param id
	 * @param body
	 */
	public MessageImpl(String id, byte[] body, Map<String, String> properties) {
		this.id = id;
		this.body = body;
		this.properties = properties;
	}

	@Override
	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	@Override
	public String getProperty(String name) {
		return properties.get(name);
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
	public String getGroupId() {
		return properties.get(GROUP_ID);
	}
	
	@Override
	public void setGroupId(String groupId) {
		properties.put(GROUP_ID, groupId);
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		id = input.readString();
		body = input.read();
		properties = input.readMap();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(id);
		output.write(body);
		output.writeMap(properties);
	}
}
