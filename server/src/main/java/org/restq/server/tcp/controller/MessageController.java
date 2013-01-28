/**
 * 
 */
package org.restq.server.tcp.controller;

import org.restq.core.Response;
import org.restq.core.Response.Status;
import org.restq.messaging.Destination;
import org.restq.messaging.EnqueueRequest;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.server.router.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author ganeshs
 *
 */
@Controller("tcpMessageController")
public class MessageController {

	@Autowired
	private RouterService routerService;
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	public Response sendMessage(EnqueueRequest request) {
		Destination destination = destinationRepository.find(request.getDestinationId());
		routerService.routeMessage(destination, request.getMessage());
		return new Response(Status.success);
	}
}
