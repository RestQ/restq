/**
 * 
 */
package org.restq.core.impl;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.restq.core.Queue;
import org.restq.core.RestQException;
import org.restq.core.ServerMessage;
import org.restq.core.utility.PriorityQueue;
import org.restq.core.utility.impl.PriorityQueueImpl;

/**
 * @author ganeshs
 *
 */
public class QueueImpl extends DestinationImpl implements Queue {
	
	private boolean durable;
	
	private PriorityQueue<ServerMessage> priorityQueue = new PriorityQueueImpl<ServerMessage>();
	
	private RandomAccessFile file;
	
	private FileChannel channel;
	
	/**
	 * <p>Default Constructor</p>
	 */
	public QueueImpl() {
		try {
			file = new RandomAccessFile("messages", "rw");
		} catch (FileNotFoundException e) {
			throw new RestQException(e);
		}
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
//		priorityQueue.enqueue(message, message.getPriority());
		try {
			message.writeData(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
