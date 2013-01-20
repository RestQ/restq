/**
 * 
 */
package org.restq.core.service;

import org.restq.core.Queue;
import org.restq.core.ServerMessage;

/**
 * @author ganeshs
 *
 */
public interface MessagingService {

	void sendMessage(Queue queue, ServerMessage message);
}
