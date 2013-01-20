/**
 * 
 */
package org.restq.core.transaction.impl;

import java.util.HashSet;
import java.util.Set;

import org.restq.core.transaction.Transaction;
import org.restq.core.transaction.TransactionOperation;

/**
 * @author ganeshs
 *
 */
public class TransactionImpl implements Transaction {
	
	private State state = State.active;
	
	private Set<TransactionOperation> operations = new HashSet<TransactionOperation>();
	
	/**
	 * @param operation
	 */
	public void addOperation(TransactionOperation operation) {
		operations.add(operation);
	}

	@Override
	public void commit() {

	}

	@Override
	public void rollback() {

	}

	@Override
	public State getState() {
		return state;
	}
}
