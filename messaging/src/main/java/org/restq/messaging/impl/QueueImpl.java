/**
 * 
 */
package org.restq.messaging.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.restq.core.RestQException;
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
	
	private RandomAccessFile file;
	
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
	 * @param name
	 * @param durable
	 */
	public QueueImpl(String name, boolean durable) {
		super(name);
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

	@Override
	public void readData(DataInput input) throws IOException {
		super.readData(input);
		durable = input.readBoolean();
		
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		output.writeBoolean(durable);
	}
}
