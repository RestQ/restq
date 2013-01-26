/**
 * 
 */
package org.restq.server.tcp.controller;

import org.restq.core.Response;
import org.restq.core.Response.Status;
import org.restq.messaging.Destination;
import org.restq.messaging.EnqueueRequest;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ganeshs
 *
 */
@Component
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	public Response sendMessage(EnqueueRequest request) {
		Destination destination = destinationRepository.find(request.getDestinationId());
		messageService.sendMessage(destination, request.getMessage());
		return new Response(Status.success);
	}
}
