/**
 * 
 */
package org.restq.utility.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.util.Iterator;

import org.restq.core.server.Identifiable;
import org.restq.core.server.RestQException;
import org.restq.utility.PriorityQueue;
import org.restq.utility.impl.PriorityQueueImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author ganeshs
 *
 */
public class PriorityQueueImplTest {
	
	private PriorityQueue<QueueItem> priorityQueue;
	
	@BeforeMethod
	public void setup() {
		priorityQueue = new PriorityQueueImpl<QueueItem>();
	}

	@Test
	public void shouldEnqueueItemToQueue() {
		priorityQueue.enqueue(new QueueItem("test_item"));
		assertEquals(priorityQueue.size(), 1);
	}
	
	@Test
	public void shouldDequeueItemFromQueue() {
		priorityQueue.enqueue(new QueueItem("test_item"));
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item"));
	}
	
	@Test
	public void shouldEnqueueItemToQueueWithPriority() {
		priorityQueue.enqueue(new QueueItem("test_item"), 3);
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item"));
	}
	
	@Test
	public void shouldDequeueItemFromQueueWithPriority() {
		priorityQueue.enqueue(new QueueItem("test_item"), 3);
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item"));
	}
	
	@Test
	public void shouldDequeueItemsFromQueueInOrderOfPriority() {
		priorityQueue.enqueue(new QueueItem("test_item1"), 3);
		priorityQueue.enqueue(new QueueItem("test_item2"), 1);
		priorityQueue.enqueue(new QueueItem("test_item3"), 9);
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item3"));
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item1"));
		assertEquals(priorityQueue.dequeue(), new QueueItem("test_item2"));
	}
	
	@Test
	public void shouldReturnSizeOfTheQueue() {
		priorityQueue.enqueue(new QueueItem("test_item1"));
		priorityQueue.enqueue(new QueueItem("test_item2"));
		priorityQueue.enqueue(new QueueItem("test_item3"));
		assertEquals(priorityQueue.size(), 3);
	}
	
	@Test
	public void shouldReturnSizeOfTheQueueWithPriority() {
		priorityQueue.enqueue(new QueueItem("test_item1"), 3);
		priorityQueue.enqueue(new QueueItem("test_item2"), 1);
		priorityQueue.enqueue(new QueueItem("test_item3"), 9);
		assertEquals(priorityQueue.size(), 3);
	}
	
	@Test
	public void shouldClearTheQueue() {
		priorityQueue.enqueue(new QueueItem("test_item1"));
		priorityQueue.enqueue(new QueueItem("test_item2"));
		priorityQueue.enqueue(new QueueItem("test_item3"));
		priorityQueue.clear();
		assertEquals(priorityQueue.size(), 0);
	}
	
	@Test
	public void shouldClearTheQueueWithPriority() {
		priorityQueue.enqueue(new QueueItem("test_item1"), 3);
		priorityQueue.enqueue(new QueueItem("test_item2"), 1);
		priorityQueue.enqueue(new QueueItem("test_item3"), 9);
		priorityQueue.clear();
		assertEquals(priorityQueue.size(), 0);
	}
	
	@Test
	public void shouldReturnEmptyIteratorForEmptyQueue() {
		Iterator<QueueItem> iterator = priorityQueue.iterator();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void shouldBeAbleToIterateOverTheQueue() {
		priorityQueue.enqueue(new QueueItem("test_item1"));
		priorityQueue.enqueue(new QueueItem("test_item2"));
		priorityQueue.enqueue(new QueueItem("test_item3"));
		Iterator<QueueItem> iterator = priorityQueue.iterator();
		assertNotNull(iterator);
		assertEquals(iterator.next(), new QueueItem("test_item1"));
		assertEquals(iterator.next(), new QueueItem("test_item2"));
		assertEquals(iterator.next(), new QueueItem("test_item3"));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void shouldBeAbleToIterateOverTheQueueWithPriority() {
		priorityQueue.enqueue(new QueueItem("test_item1"), 3);
		priorityQueue.enqueue(new QueueItem("test_item2"), 1);
		priorityQueue.enqueue(new QueueItem("test_item3"), 9);
		Iterator<QueueItem> iterator = priorityQueue.iterator();
		assertNotNull(iterator);
		assertEquals(iterator.next(), new QueueItem("test_item3"));
		assertEquals(iterator.next(), new QueueItem("test_item1"));
		assertEquals(iterator.next(), new QueueItem("test_item2"));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void shouldAllowEnqueueForValidPriorities() {
		priorityQueue = new PriorityQueueImpl<QueueItem>(4);
		priorityQueue.enqueue(new QueueItem("test_item1"), 1);
		priorityQueue.enqueue(new QueueItem("test_item2"), 2);
		priorityQueue.enqueue(new QueueItem("test_item3"), 3);
		priorityQueue.enqueue(new QueueItem("test_item4"), 4);
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionOnEnqueueWithWrongPriority() {
		priorityQueue = new PriorityQueueImpl<QueueItem>(4);
		priorityQueue.enqueue(new QueueItem("test_item"), 5);
	}
	
	private static class QueueItem implements Identifiable {
		
		private String item;
		
		public QueueItem(String item) {
			this.item = item;
		}

		@Override
		public Serializable getId() {
			return item;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			QueueItem other = (QueueItem) obj;
			if (item == null) {
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			return true;
		}
		
	}
}
