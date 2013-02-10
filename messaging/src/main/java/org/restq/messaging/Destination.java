/**
 * 
 */
package org.restq.messaging;

import org.restq.core.DataSerializable;
import org.restq.core.Identifiable;

/**
 * @author ganeshs
 *
 */
public interface Destination extends DataSerializable, Identifiable {
	
	String getId();

	String getName();
	
	long getMessageCount();
	
	void addMessage(ServerMessage message);
}
