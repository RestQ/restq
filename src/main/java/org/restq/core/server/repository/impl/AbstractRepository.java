/**
 * 
 */
package org.restq.core.server.repository.impl;

import java.io.Serializable;

import org.restq.core.server.repository.Repository;

/**
 * @author ganeshs
 *
 */
public abstract class AbstractRepository<T> implements Repository<T> {

	@Override
	public void create(T entity) {
		
	}

	@Override
	public void save(T entity) {
		
	}

	@Override
	public void find(Serializable id) {
		
	}

}
