/**
 * 
 */
package org.restq.server.http;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restq.server.http.controller.DestinationController;
import org.restq.server.http.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.plugin.AbstractPlugin;

/**
 * @author ganeshs
 *
 */
@Component
public class RoutesPlugin extends AbstractPlugin {
	
	@Autowired
	@Qualifier(value="httpMessageController")
	private MessageController messageController;
	
	@Autowired
	private DestinationController destinationController;

	@Override
	public AbstractPlugin register(RestExpress server) {
		defineRoutes(server);
		return super.register(server);
	}

	protected void defineRoutes(RestExpress server) {
		server.uri("/queues/{destination_name}/send", messageController).action("sendMessage", HttpMethod.POST);
		server.uri("/queues/", destinationController).action("createQueues", HttpMethod.POST);
		server.uri("/topics/", destinationController).action("createTopics", HttpMethod.POST);
		server.uri("/queues/", destinationController).action("getQueues", HttpMethod.GET);
	}
}