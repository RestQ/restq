/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.utility.PriorityQueue;
import org.restq.core.utility.impl.PriorityQueueImpl;
import org.restq.messaging.Queue;
import org.restq.messaging.ServerMessage;

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
	
	/**
	 * @param id
	 * @param name
	 * @param durable
	 * @param priorityQueue
	 */
	public QueueImpl(String id, String name, boolean durable, PriorityQueue<ServerMessage> priorityQueue) {
		this(id, name, durable);
		this.priorityQueue = priorityQueue;
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

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
		durable = input.readBoolean();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		output.writeBoolean(durable);
	}
}
