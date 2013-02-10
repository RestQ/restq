/**
 * 
 */
package org.restq.messaging.repository;

import java.util.List;

import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.springframework.stereotype.Repository;

/**
 * @author ganeshs
 *
 */
@Repository
public interface MessageRepository {

	ServerMessage find(Destination destination, String id);
	
	void persist(Destination destination, ServerMessage message);
	
	void delete(Destination destination, ServerMessage message);
	
	void refresh(Destination destination);
	
	List<ServerMessage> findAll(Destination destination);
}
