/**
 * 
 */
package org.restq.messaging.impl;

import org.restq.messaging.ServerMessage;
import org.restq.messaging.Topic;

/**
 * @author ganeshs
 *
 */
public class TopicImpl extends DestinationImpl implements Topic {

	public TopicImpl() {
	}

	public TopicImpl(String name) {
		super(name);
	}

	@Override
	public void addMessage(ServerMessage message) {
		
	}

}
