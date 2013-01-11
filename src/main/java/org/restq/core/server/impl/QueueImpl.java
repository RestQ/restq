/**
 * 
 */
package org.restq.core.server.impl;

import org.restq.core.server.Queue;
import org.restq.core.server.ServerMessage;
import org.restq.utility.PriorityQueue;
import org.restq.utility.impl.PriorityQueueImpl;

/**
 * @author ganeshs
 *
 */
public class QueueImpl extends DestinationImpl implements Queue {
	
	private boolean durable;
	
	private PriorityQueue<ServerMessage> priorityQueue = new PriorityQueueImpl<ServerMessage>();
	
	/**
	 * <p>Default Constructor</p>
	 */
	public QueueImpl() {
	}
	
	/**
	 * @param id
	 * @param name
	 * @param durable
	 */
	public QueueImpl(String id, String name, boolean durable) {
		super(id, name);
		this.durable = durable;
	}

	@Override
	public void addMessage(ServerMessage message) {
		priorityQueue.enqueue(message, message.getPriority());
	}

	/**
	 * @return the durable
	 */
	public boolean isDurable() {
		return durable;
	}

	/**
	 * @param durable the durable to set
	 */
	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	/**
	 * @return the priorityQueue
	 */
	public PriorityQueue<ServerMessage> getPriorityQueue() {
		return priorityQueue;
	}

	/**
	 * @param priorityQueue the priorityQueue to set
	 */
	public void setPriorityQueue(PriorityQueue<ServerMessage> priorityQueue) {
		this.priorityQueue = priorityQueue;
	}

}
