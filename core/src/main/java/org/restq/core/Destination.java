/**
 * 
 */
package org.restq.core;

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
