/**
 * 
 */
package org.restq.messaging.service;

import org.restq.messaging.Queue;
import org.restq.messaging.Topic;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface DestinationService {

	void createQueue(Queue queue);
	
	void createTopic(Topic topic);
}
