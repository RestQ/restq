/**
 * 
 */
package org.restq.core.controller;

import java.util.UUID;

import org.restq.core.Queue;
import org.restq.core.ServerMessage;
import org.restq.core.impl.QueueImpl;
import org.restq.core.impl.ServerMessageImpl;
import org.springframework.stereotype.Controller;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author ganeshs
 *
 */
@Controller
public class MessageController {
	
	private Queue queue = new QueueImpl();

	public void sendMessage(Request request, Response response) {
		ServerMessage message = new ServerMessageImpl(UUID.randomUUID().toString(), request.getBody().array());
		queue.addMessage(message);
	}
}
