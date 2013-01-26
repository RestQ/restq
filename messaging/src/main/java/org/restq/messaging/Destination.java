/**
 * 
 */
package org.restq.messaging;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public interface Destination extends DataSerializable {

	String getName();
	
	long getMessageCount();
	
	void addMessage(ServerMessage message);
}
