/**
 * 
 */
package org.restq.core.server.repository;

import java.io.Serializable;

/**
 * @author ganeshs
 *
 */
public interface Repository<T> {

	void create(T entity);
	
	void save(T entity);
	
	void find(Serializable id);
}
