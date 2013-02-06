/**
 * 
 */
package org.restq.messaging;


/**
 * @author ganeshs
 *
 */
public interface Subscriber extends Consumer {
	
	String getBoundDestination();
	
	String getHttpUrl();
	
	String getHttpMethod();
	
	boolean isDurable();
}
