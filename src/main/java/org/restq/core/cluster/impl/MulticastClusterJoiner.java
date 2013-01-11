/**
 * 
 */
package org.restq.core.cluster.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.core.cluster.ClusterJoiner;
import org.restq.core.cluster.JoinRequest;
import org.restq.core.cluster.JoinResponse;
import org.restq.core.cluster.Member;
import org.restq.core.cluster.MulticastService;
import org.restq.core.cluster.Node;

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class MulticastClusterJoiner implements ClusterJoiner {
	
	private MulticastService multicastService;
	
	private int maxRetries;
	
	private static final Logger logger = Logger.getLogger(MulticastClusterJoiner.class);
	
	public MulticastClusterJoiner() {
	}
	
	public MulticastClusterJoiner(MulticastService service, int maxRetries) {
		this.multicastService = service;
		this.maxRetries = maxRetries;
	}

	@Override
	public void join(Node node) {
		try {
			JoinRequest request = new JoinRequest(node.getMember());
			for (int i = 0; i < maxRetries; i++) {
				multicastService.send(request);
				if (node.hasJoined()) {
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			logger.info("Couldn't find a master in the cluster. Assuming myself as a master");
			Set<Member> members = new HashSet<Member>();
			members.add(node.getMember());
			multicastService.send(new JoinResponse(members, node.getMember()));
		} catch (IOException e) {
			logger.error("Failed while joining the node - " + node + " to the cluster");
			throw new RestQException(e);
		}
	}

}
