/**
 * 
 */
package org.restq.utility.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.restq.core.server.Identifiable;
import org.restq.utility.PriorityQueue;

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class PriorityQueueImpl<T extends Identifiable> implements PriorityQueue<T> {
	
	private int maxPriorities;
	
	private List<LinkedHashMap<Serializable, T>> priorityMaps;
	
	private static final int DEFAULT_PRIORITY = 5;
	
	private static final int DEFAULT_MAX_PRIORITIES = 10;
	
	/**
	 * <p>Default constructor</p>
	 */
	public PriorityQueueImpl() {
		this(DEFAULT_MAX_PRIORITIES);
	}
	
	/**
	 * <p>Constructor taking in maxPriorities</p>
	 * 
	 * @param maxPriorities the maximum number of priorities
	 */
	public PriorityQueueImpl(int maxPriorities) {
		this.maxPriorities = maxPriorities;
		init();
	}
	
	protected void init() {
		priorityMaps = new ArrayList<LinkedHashMap<Serializable,T>>();
		priorityMaps.add(0, null);
		for (int i = 1; i <= maxPriorities; i++) {
			priorityMaps.add(i, new LinkedHashMap<Serializable, T>());
		}
	}

	@Override
	public void enqueue(T item) {
		enqueue(item, DEFAULT_PRIORITY);
	}

	@Override
	public void enqueue(T item, int priority) {
		if (priority > maxPriorities) {
			throw new RestQException("Priority - " + priority + " is greater than maximum priority - " + maxPriorities);
		}
		priorityMaps.get(priority).put(item.getId(), item);
	}

	@Override
	public T dequeue() {
		LinkedHashMap<Serializable, T> map = null;
		for (int i = maxPriorities; i > 0; i--) {
			map = priorityMaps.get(i);
			for (Entry<Serializable, T> entry : map.entrySet()) {
				return map.remove(entry.getKey());
			}
		}
		return null;
	}

	@Override
	public int size() {
		int count = 0;
		for (int i = maxPriorities; i > 0; i--) {
			count += priorityMaps.get(i).size();
		}
		return count;
	}

	@Override
	public void clear() {
		for (int i = maxPriorities; i > 0; i--) {
			priorityMaps.get(i).clear();
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new PriorityQueueIterator();
	}


	/**
	 * @author ganeshs
	 *
	 * @param <T>
	 */
	private class PriorityQueueIterator implements Iterator<T> {
		
		private List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
		
		private Iterator<T> lastIterator;
		
		public PriorityQueueIterator() {
			for (int i = maxPriorities; i > 0; i--) {
				iterators.add(priorityMaps.get(i).values().iterator());
			}
		}
		
		@Override
		public boolean hasNext() {
			setLastIterator();
			if (lastIterator == null) {
				return false;
			}
			return lastIterator.hasNext();
		}

		@Override
		public T next() {
			setLastIterator();
			if (lastIterator == null) {
				return null;
			}
			return lastIterator.next();
		}

		@Override
		public void remove() {
			setLastIterator();
			if (lastIterator == null) {
				return;
			}
			lastIterator.remove();
		}
		
		private void setLastIterator() {
			if (lastIterator == null || !lastIterator.hasNext()) {
				for (Iterator<T> iterator : iterators) {
					lastIterator = iterator;
					if (lastIterator.hasNext()) {
						break;
					}
				}
			}
		}
	}
}
