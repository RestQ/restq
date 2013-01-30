/**
 * 
 */
package org.restq.messaging.service.impl;

import static org.mockito.Mockito.*;

import org.restq.messaging.Queue;
import org.restq.messaging.Topic;
import org.restq.messaging.repository.DestinationRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class DestinationServiceImplTest {
	
	private DestinationServiceImpl destinationService;
	
	private DestinationRepository destinationRepository;
	
	@BeforeMethod
	public void setup() {
		destinationRepository = mock(DestinationRepository.class);
		destinationService = new DestinationServiceImpl(destinationRepository);
	}

	@Test
	public void shouldCreateQueue() {
		Queue queue = mock(Queue.class);
		destinationService.createQueue(queue);
		verify(destinationRepository).create(queue);
	}
	
	@Test
	public void shouldCreateTopic() {
		Topic topic = mock(Topic.class);
		destinationService.createTopic(topic);
		verify(destinationRepository).create(topic);
	}
}
