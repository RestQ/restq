/**
 * 
 */
package org.restq.core.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.restq.core.ServerMessage;

/**
 * @author ganeshs
 *
 */
public class ServerMessageImpl extends MessageImpl implements ServerMessage {

	/**
	 * @param id
	 * @param body
	 */
	public ServerMessageImpl(Serializable id, byte[] body) {
		super(id, body);
	}

	@Override
	public void readData(DataInput input) throws IOException {
		super.readData(input);
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
	}
}
