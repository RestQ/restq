/**
 * 
 */
package org.restq.messaging;

import java.util.Set;

/**
 * @author ganeshs
 *
 */
public interface Topic extends Destination {

	void addSubscriber(Subscriber subscriber);
	
	void removeSubscriber(Subscriber subscriber);
	
	Set<Subscriber> getSubscribers();
}
