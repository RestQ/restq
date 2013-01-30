/**
 * 
 */
package org.restq.messaging.service.impl;

import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author ganeshs
 *
 */
public class MessageServiceImplTest {

	@Test
	public void shouldSendMessageToDestination() {
		MessageServiceImpl messageService = new MessageServiceImpl();
		Destination destination = mock(Destination.class);
		ServerMessage message = mock(ServerMessage.class);
		messageService.sendMessage(destination, message);
		verify(destination).addMessage(message);
	}
}
