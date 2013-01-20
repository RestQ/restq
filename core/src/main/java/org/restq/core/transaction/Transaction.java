/**
 * 
 */
package org.restq.core.transaction;

/**
 * @author ganeshs
 *
 */
public interface Transaction {
	
	public enum State {
		active, committed, rolledback
	}

	void commit();
	
	void rollback();
	
	State getState();
}
