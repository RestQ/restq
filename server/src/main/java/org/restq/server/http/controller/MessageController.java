/**
 * 
 */
package org.restq.server.http.controller;

import java.util.UUID;

import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.impl.ServerMessageImpl;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.NotFoundException;

/**
 * @author ganeshs
 *
 */
@Controller("httpMessageController")
public class MessageController {
	
	@Autowired
	private RouterService routerService;
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	public void sendMessage(Request request, Response response) {
		String destinationName = request.getUrlDecodedHeader("destination_name", "Destination name not specified");
		Destination destination = destinationRepository.find(destinationName);
		if (destination == null) {
			throw new NotFoundException("Destination " + destinationName + " is not found");
		}
		ServerMessage message = new ServerMessageImpl(UUID.randomUUID().toString(), request.getBody().array());
		routerService.routeMessage(destination, message);
	}
}
