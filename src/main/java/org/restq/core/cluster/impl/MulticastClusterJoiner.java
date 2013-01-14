/**
 * 
 */
package org.restq.core.cluster.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.core.cluster.ClusterJoiner;
import org.restq.core.cluster.JoinRequest;
import org.restq.core.cluster.MasterInfo;
import org.restq.core.cluster.Member;
import org.restq.core.cluster.MulticastService;
import org.restq.core.cluster.Node;
import org.restq.core.cluster.UnjoinRequest;


/**
 * @author ganeshs
 *
 */
public class MulticastClusterJoiner implements ClusterJoiner {
	
	private MulticastService multicastService;
	
	private int maxRetries;
	
	private static final Logger logger = Logger.getLogger(MulticastClusterJoiner.class);
	
	/**
	 * <p>Default constructor</p>
	 */
	public MulticastClusterJoiner() {
	}
	
	/**
	 * @param service
	 * @param maxRetries
	 */
	public MulticastClusterJoiner(MulticastService service, int maxRetries) {
		this.multicastService = service;
		this.maxRetries = maxRetries;
	}

	@Override
	public void join(Node node) {
		JoinRequest request = new JoinRequest(node.getMember());
		for (int i = 0; i < maxRetries; i++) {
			multicastService.send(request);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			if (node.hasJoined()) {
				return;
			}
		}
		logger.info("Couldn't find a master in the cluster");
		joinAsMaster(node);
	}
	
	public void joinAsMaster(Node node) {
		logger.info("Joining as a master");
		multicastService.send(new MasterInfo(node.getMember()));
	}
	
	@Override
	public void unjoin(Node node) {
		UnjoinRequest request = new UnjoinRequest(node.getMember());
		multicastService.send(request);
	}
}
