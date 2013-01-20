/**
 * 
 */
package org.restq.server.http;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restq.core.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
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
	private MessageController messageController;

	@Override
	public AbstractPlugin register(RestExpress server) {
		defineRoutes(server);
		return super.register(server);
	}

	protected void defineRoutes(RestExpress server) {
		server.uri("/send", messageController).action("sendMessage", HttpMethod.POST);
	}
}
