/**
 * 
 */
package org.restq.messaging.service.impl;

import org.apache.log4j.Logger;
import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.service.MessageService;

/**
 * @author ganeshs
 *
 */
public class MessageServiceImpl implements MessageService {
	
	private static Logger logger = Logger.getLogger(MessageServiceImpl.class);

	@Override
	public void sendMessage(Destination destination, ServerMessage message) {
		logger.info("Received a message " + message);
		destination.addMessage(message);
	}

}
