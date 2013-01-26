/**
 * 
 */
package org.restq.messaging;

import java.util.Map;

import org.restq.core.DataSerializable;
import org.restq.core.Identifiable;

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
