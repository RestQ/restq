/**
 * 
 */
package org.restq.messaging.service.impl;

import org.apache.log4j.Logger;
import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.Subscriber;
import org.restq.messaging.Topic;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.repository.MessageRepository;
import org.restq.messaging.service.MessageService;
import org.restq.messaging.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private RouterService routerService;
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	private static Logger logger = Logger.getLogger(MessageServiceImpl.class);
	
	public MessageServiceImpl() {
	}

	/**
	 * @param routerService
	 * @param destinationRepository
	 * @param messageRepository
	 */
	public MessageServiceImpl(RouterService routerService, DestinationRepository destinationRepository, MessageRepository messageRepository) {
		this.destinationRepository = destinationRepository;
		this.routerService = routerService;
		this.messageRepository = messageRepository;
	}

	@Override
	public void sendMessage(Destination destination, ServerMessage message) {
		logger.info("Received a message " + message);
		if (destination instanceof Topic) {
			Topic topic = (Topic) destination;
			sendMessageToSubscribers(message, topic);
		} else {
			destination.addMessage(message);
			messageRepository.persist(destination, message);
		}
	}
	
	protected void sendMessageToSubscribers(ServerMessage message, Topic topic) {
		for (Subscriber subscriber : topic.getSubscribers()) {
			Destination destination = destinationRepository.find(subscriber.getBoundDestination());
			if (message.matches(subscriber.getFilters())) {
				routerService.routeMessage(destination, message);
			}
		}
	}

}
