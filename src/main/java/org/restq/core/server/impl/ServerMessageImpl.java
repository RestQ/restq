/**
 * 
 */
package org.restq.core.server.impl;

import java.io.Serializable;

import org.restq.core.server.ServerMessage;

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

}
