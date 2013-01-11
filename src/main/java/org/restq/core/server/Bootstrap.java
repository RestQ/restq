/**
 * 
 */
package org.restq.core.server;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Log4JLoggerFactory;
import org.restq.core.controller.MessageController;

import com.strategicgains.restexpress.RestExpress;

/**
 * @author ganeshs
 *
 */
public class Bootstrap {
	
	private RestExpress server;
	
	private boolean started;
	
	private final Logger logger = Logger.getLogger(Bootstrap.class);
	
	public Bootstrap() {
		init();
	}
	
	protected void init() {
		server = new RestExpress();
		server.setPort(3000);
		server.uri("/send", new MessageController()).action("sendMessage", HttpMethod.POST);
		InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
		mapExceptions(server);
	}
	
	public void start() {
		server.bind();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Bootstrap.this.stop();
			}
		});
		started = true;
	}
	
	public void awaitShutdown() {
		server.awaitShutdown();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void stop() {
		logger.info("Stopping the server");
		server.shutdown();
        started = false;
	}
	
	private void mapExceptions(RestExpress server) {
	}
	
	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.start();
		bootstrap.awaitShutdown();
	}

}
