/**
 * 
 */
package org.restq.core.server;

import java.util.Map;

/**
 * @author ganeshs
 *
 */
public interface Message extends Identifiable {

	byte[] getBody();
	
	Map<String, String> getProperties();
	
	void addProperty(String name, String value);
	
	void getProperty(String name);
	
	int getPriority();
}
