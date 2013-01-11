/**
 * 
 */
package org.restq.core.server;

/**
 * @author ganeshs
 *
 */
public interface Queue extends Destination {

	boolean isDurable();
	
}