/**
 * 
 */
package org.restq.core;

/**
 * @author ganeshs
 *
 */
public class ConflictException extends RestQException {

	public ConflictException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ConflictException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConflictException(Throwable cause) {
		super(cause);
	}

}
