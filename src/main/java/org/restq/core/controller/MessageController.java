/**
 * 
 */
package org.restq.core.controller;

import java.util.UUID;

import org.restq.core.server.Queue;
import org.restq.core.server.ServerMessage;
import org.restq.core.server.impl.QueueImpl;
import org.restq.core.server.impl.ServerMessageImpl;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author ganeshs
 *
 */
public class MessageController {

	public void sendMessage(Request request, Response response) {
		Queue queue = new QueueImpl();
		ServerMessage message = new ServerMessageImpl(UUID.randomUUID().toString(), request.getBody().array());
		queue.addMessage(message);
	}
}
