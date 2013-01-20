/**
 * 
 */
package org.restq.cluster;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ganeshs
 *
 */
public class RequestMapper {
	
	private Map<Class<? extends Request>, RequestMapping> mappings = new HashMap<Class<? extends Request>, RequestMapping>();

	/**
	 * @param requestClass
	 * @param controller
	 * @param action
	 */
	public void addMapping(Class<? extends Request> requestClass, Object controller, Method action) {
		this.mappings.put(requestClass, new RequestMapping(requestClass, controller, action));
	}
	
	/**
	 * @param requestClass
	 * @return
	 */
	public RequestMapping mappingFor(Class<? extends Request> requestClass) {
		return this.mappings.get(requestClass);
	}

	/**
	 * @return the mappings
	 */
	public Map<Class<? extends Request>, RequestMapping> getMappings() {
		return mappings;
	}

	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(Map<Class<? extends Request>, RequestMapping> mappings) {
		this.mappings = mappings;
	}
}
