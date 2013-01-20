/**
 * 
 */
package org.restq.core;

import java.util.Map;

/**
 * @author ganeshs
 *
 */
public interface Message extends Identifiable, DataSerializable {

	byte[] getBody();
	
	Map<String, String> getProperties();
	
	void addProperty(String name, String value);
	
	void getProperty(String name);
	
	int getPriority();
}
