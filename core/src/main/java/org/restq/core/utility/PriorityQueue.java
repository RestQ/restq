/**
 * 
 */
package org.restq.core.utility;

import java.util.Iterator;

import org.restq.core.Identifiable;

/**
 * @author ganeshs
 *
 */
public interface PriorityQueue<T extends Identifiable> {

	void enqueue(T item);
	
	void enqueue(T item, int priority);
	
	T dequeue();
	
	int size();
	
	void clear();
	
	Iterator<T> iterator();
}
