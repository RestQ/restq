/**
 * 
 */
package org.restq.server.http.controller;

import java.util.UUID;

import org.restq.messaging.Queue;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.impl.QueueImpl;
import org.restq.messaging.impl.ServerMessageImpl;
import org.restq.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author ganeshs
 *
 */
@Controller("httpMessageController")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	private Queue queue = new QueueImpl();

	public void sendMessage(Request request, Response response) {
		ServerMessage message = new ServerMessageImpl(UUID.randomUUID().toString(), request.getBody().array());
		messageService.sendMessage(queue, message);
		queue.addMessage(message);
	}
}
