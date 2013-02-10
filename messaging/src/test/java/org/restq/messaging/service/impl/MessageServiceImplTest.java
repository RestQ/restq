/**
 * 
 */
package org.restq.messaging.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.restq.messaging.Destination;
import org.restq.messaging.Queue;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.Subscriber;
import org.restq.messaging.Topic;
import org.restq.messaging.impl.SubscriberImpl;
import org.restq.messaging.impl.TopicImpl;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.repository.MessageRepository;
import org.restq.messaging.service.RouterService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class MessageServiceImplTest {
	
	private DestinationRepository destinationRepository;
	
	private RouterService routerService;
	
	private MessageServiceImpl messageService;
	
	private MessageRepository messageRepository;
	
	@BeforeMethod
	public void setup() {
		this.destinationRepository = mock(DestinationRepository.class);
		this.routerService = mock(RouterService.class);
		this.messageRepository = mock(MessageRepository.class);
		this.messageService = new MessageServiceImpl(routerService, destinationRepository, messageRepository);
	}

	@Test
	public void shouldSendMessageToQueue() {
		Queue destination = mock(Queue.class);
		ServerMessage message = mock(ServerMessage.class);
		messageService.sendMessage(destination, message);
		verify(destination).addMessage(message);
		verify(messageRepository).persist(destination, message);
	}
	
	@Test
	public void shouldSendMessageToTopic() {
		Topic destination = new TopicImpl();
		ServerMessage message = mock(ServerMessage.class);
		Subscriber subscriber1 = new SubscriberImpl("testId", "testDest", "testBD1", "dummyurl", "post", true);
		Subscriber subscriber2 = new SubscriberImpl("testId1", "testDest1", "testBD2", "dummyurl", "post", true);
		destination.addSubscriber(subscriber1);
		destination.addSubscriber(subscriber2);
		
		Destination dest1 = mock(Destination.class);
		Destination dest2 = mock(Destination.class);
		when(destinationRepository.find(subscriber1.getBoundDestination())).thenReturn(dest1);
		when(destinationRepository.find(subscriber2.getBoundDestination())).thenReturn(dest2);
		when(message.matches(any(List.class))).thenReturn(true);
		messageService.sendMessage(destination, message);
		verify(routerService).routeMessage(dest1, message);
		verify(routerService).routeMessage(dest2, message);
	}
}
