/**
 * 
 */
package org.restq.server;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.restq.server.http.RestServer;
import org.restq.server.tcp.TcpServer;
import org.restq.server.udp.MulticastServer;
import org.restq.server.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ganeshs
 *
 */
@Component
public class Bootstrap {
	
	@Autowired
	private RestServer restServer;
	
	@Autowired
	private TcpServer tcpServer;
	
	@Autowired
	private MulticastServer multicastServer;
	
	private final Logger logger = Logger.getLogger(Bootstrap.class);
	
	public Bootstrap() {
	}
	
	@PostConstruct
	public void init() {
		restServer.init();
		tcpServer.init();
		multicastServer.init();
	}
	
	public void start() {
		multicastServer.start();
		tcpServer.start();
		restServer.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Bootstrap.this.stop();
			}
		});
	}
	
	public void stop() {
		logger.info("Stopping the server");
		restServer.stop();
        tcpServer.stop();
        multicastServer.stop();
	}
	
	public static void main(String[] args) {
		Bootstrap bootstrap = SpringUtil.getBean(Bootstrap.class);
		bootstrap.start();
	}

}
