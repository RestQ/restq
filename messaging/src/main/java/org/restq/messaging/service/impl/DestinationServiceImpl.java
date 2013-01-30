/**
 * 
 */
package org.restq.messaging.service.impl;

import org.restq.core.ConflictException;
import org.restq.messaging.Queue;
import org.restq.messaging.Topic;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class DestinationServiceImpl implements DestinationService {
	
	@Autowired
	private DestinationRepository destinationRepository;
	
	public DestinationServiceImpl() {
	}
	
	public DestinationServiceImpl(DestinationRepository repository) {
		this.destinationRepository = repository;
	}

	@Override
	public void createQueue(Queue queue) {
		if (destinationRepository.find(queue.getName()) != null) {
			throw new ConflictException("Queue " + queue.getName() + " already exist");
		}
		destinationRepository.create(queue);
	}

	@Override
	public void createTopic(Topic topic) {
		if (destinationRepository.find(topic.getName()) != null) {
			throw new ConflictException("Topic " + topic.getName() + " already exist");
		}
		destinationRepository.create(topic);
	}

}
