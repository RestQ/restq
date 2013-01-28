/**
 * 
 */
package org.restq.server.router.service;

import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface RouterService {

	void routeMessage(Destination destination, ServerMessage message);
}
