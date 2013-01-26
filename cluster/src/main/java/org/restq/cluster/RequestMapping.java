/**
 * 
 */
package org.restq.cluster;

import java.lang.reflect.Method;

import org.restq.core.Request;
import org.restq.core.Response;
import org.restq.core.RestQException;

/**
 * @author ganeshs
 *
 */
public class RequestMapping {

	private Class<? extends Request> requestClass;
	
	private Object controller;
	
	private Method action;
	
	public RequestMapping() {
	}

	/**
	 * @param requestClass
	 * @param controller
	 * @param action
	 */
	public RequestMapping(Class<? extends Request> requestClass, Object controller, Method action) {
		this.requestClass = requestClass;
		this.controller = controller;
		this.action = action;
	}

	/**
	 * @return the requestClass
	 */
	public Class<? extends Request> getRequestClass() {
		return requestClass;
	}

	/**
	 * @param requestClass the requestClass to set
	 */
	public void setRequestClass(Class<? extends Request> requestClass) {
		this.requestClass = requestClass;
	}

	/**
	 * @return the controller
	 */
	public Object getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Object controller) {
		this.controller = controller;
	}

	/**
	 * @return the action
	 */
	public Method getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Method action) {
		this.action = action;
	}
	
	public Response invoke(Request request) {
		try {
	        return (Response) action.invoke(controller, request);
        } catch (Exception e) {
        	throw new RestQException(e.getCause());
        }
	}
}
