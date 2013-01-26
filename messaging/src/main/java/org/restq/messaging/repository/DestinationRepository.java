/**
 * 
 */
package org.restq.messaging.repository;

import org.restq.messaging.Destination;
import org.springframework.stereotype.Repository;

/**
 * @author ganeshs
 *
 */
@Repository
public interface DestinationRepository {

	Destination find(String id);
	
	void create(Destination destination);
	
	void delete(Destination destination);
	
	void save(Destination destination);
	
	void refresh();
}