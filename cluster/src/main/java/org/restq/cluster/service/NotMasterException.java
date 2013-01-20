/**
 * 
 */
package org.restq.cluster.service;

import org.restq.core.RestQException;

/**
 * @author ganeshs
 *
 */
public class NotMasterException extends RestQException {

	public NotMasterException(String message) {
		super(message);
	}

}
