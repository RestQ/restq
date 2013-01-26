/**
 * 
 */
package org.restq.messaging.service;

import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface MessageService {

	void sendMessage(Destination destination, ServerMessage message);
}
