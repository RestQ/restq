/**
 * 
 */
package org.restq.server.http;

import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Log4JLoggerFactory;
import org.restq.core.RestQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.strategicgains.restexpress.RestExpress;

/**
 * @author ganeshs
 *
 */
@Component
public class RestServer implements RestQComponent {
	
	@Autowired
	private RestExpress server;
	
	@Autowired
	private RoutesPlugin routesPlugin;
	
	@Autowired
	private LoggerPlugin loggerPlugin;
	
	private boolean started;
	
	public void init() {
		InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
		server.registerPlugin(loggerPlugin);
		server.registerPlugin(routesPlugin);
		mapExceptions(server);
	}

	@Override
	public void start() {
		server.bind();
		started = true;
	}

	@Override
	public void stop() {
		server.shutdown();
		started = false;
	}

	/**
	 * @return the server
	 */
	public RestExpress getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(RestExpress server) {
		this.server = server;
	}

	/**
	 * @return the routesPlugin
	 */
	public RoutesPlugin getRoutesPlugin() {
		return routesPlugin;
	}

	/**
	 * @param routesPlugin the routesPlugin to set
	 */
	public void setRoutesPlugin(RoutesPlugin routesPlugin) {
		this.routesPlugin = routesPlugin;
	}

	/**
	 * @return the loggerPlugin
	 */
	public LoggerPlugin getLoggerPlugin() {
		return loggerPlugin;
	}

	/**
	 * @param loggerPlugin the loggerPlugin to set
	 */
	public void setLoggerPlugin(LoggerPlugin loggerPlugin) {
		this.loggerPlugin = loggerPlugin;
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	private void mapExceptions(RestExpress server) {
	}
}
