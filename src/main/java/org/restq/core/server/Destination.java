/**
 * 
 */
package org.restq.core.server;

/**
 * @author ganeshs
 *
 */
public interface Destination {

	String getName();
	
	String getId();
	
	long getMessageCount();
	
	void addMessage(ServerMessage message);
}
