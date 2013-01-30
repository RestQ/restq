/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
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
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
	}
}
