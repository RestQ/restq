/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;
import java.util.List;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Filter;
import org.restq.messaging.ServerMessage;

/**
 * @author ganeshs
 *
 */
public class ServerMessageImpl extends MessageImpl implements ServerMessage {
	
	public ServerMessageImpl() {
	}

	/**
	 * @param id
	 * @param body
	 */
	public ServerMessageImpl(String id, byte[] body) {
		super(id, body);
	}
	
	@Override
	public boolean matches(List<Filter> filters) {
		boolean matches = true;
		for (Filter filter : filters) {
			if (getProperties().containsKey(filter.getName())) {
				matches = filter.matches(getProperty(filter.getName()));
			}
			if (! matches) {
				break;
			}
		}
		return matches;
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
	}
}
