/**
 * 
 */
package org.restq.server.tcp;

import org.restq.cluster.Node;
import org.restq.core.RestQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ganeshs
 *
 */
@Component
public class TcpServer implements RestQComponent {
	
	@Autowired
	private Node node;
	
	@Autowired
	private RequestMappingPlugin mappingPlugin;
	
	private boolean started;

	@Override
	public void init() {
		node.registerPlugin(mappingPlugin);
	}

	@Override
	public void start() {
		node.bind();
		started = true;
	}

	@Override
	public void stop() {
		node.shutdown();
		started = false;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @return the mappingPlugin
	 */
	public RequestMappingPlugin getMappingPlugin() {
		return mappingPlugin;
	}

	/**
	 * @param mappingPlugin the mappingPlugin to set
	 */
	public void setMappingPlugin(RequestMappingPlugin mappingPlugin) {
		this.mappingPlugin = mappingPlugin;
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @param started the started to set
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}
}
