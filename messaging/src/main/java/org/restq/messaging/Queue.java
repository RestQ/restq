/**
 * 
 */
package org.restq.messaging;

/**
 * @author ganeshs
 *
 */
public interface Queue extends Destination {

	boolean isDurable();
	
}