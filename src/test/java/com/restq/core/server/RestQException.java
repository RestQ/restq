/**
 * 
 */
package com.restq.core.server;

/**
 * @author ganeshs
 *
 */
public class RestQException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestQException() {
	}

	/**
	 * @param message
	 */
	public RestQException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RestQException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RestQException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RestQException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
