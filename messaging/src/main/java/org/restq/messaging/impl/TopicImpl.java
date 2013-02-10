/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.Subscriber;
import org.restq.messaging.Topic;

/**
 * @author ganeshs
 *
 */
public class TopicImpl extends DestinationImpl implements Topic {
	
	private Set<Subscriber> subscribers = new HashSet<Subscriber>();

	public TopicImpl() {
	}

	public TopicImpl(String id, String name) {
		super(id, name);
	}

	@Override
	public void addMessage(ServerMessage message) {
		
	}

	@Override
	public void addSubscriber(Subscriber subscriber) {
		subscribers.add(subscriber); 
	}
	
	@Override
	public void removeSubscriber(Subscriber subscriber) {
		subscribers.remove(subscriber);
	}
	
	@Override
	public Set<Subscriber> getSubscribers() {
		return subscribers;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
	}
}
