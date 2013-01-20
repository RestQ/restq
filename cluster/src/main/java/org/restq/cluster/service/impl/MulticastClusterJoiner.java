/**
 * 
 */
package org.restq.cluster.service.impl;

import org.apache.log4j.Logger;
import org.restq.cluster.Cluster;
import org.restq.cluster.JoinRequest;
import org.restq.cluster.JoinResponse;
import org.restq.cluster.Member;
import org.restq.cluster.Node;
import org.restq.cluster.Response;
import org.restq.cluster.Response.Status;
import org.restq.cluster.UnjoinRequest;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ConnectionManager;
import org.restq.cluster.service.ClusterJoiner;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.MulticastService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author ganeshs
 *
 */
public class MulticastClusterJoiner implements ClusterJoiner {
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private ConnectionManager connectionManager;
	
	private static final Logger logger = Logger.getLogger(MulticastClusterJoiner.class);
	
	/**
	 * <p>Default constructor</p>
	 */
	public MulticastClusterJoiner() {
	}
	
	/**
	 * @param service
	 */
	public MulticastClusterJoiner(MulticastService service) {
		this.multicastService = service;
	}

	@Override
	public void join(final Node node) {
		final Cluster cluster = node.getCluster();
		Member master = cluster.getMaster();
		if (master == null) {
			master = findMaster(node);
		}
		if (master == null) {
			logger.info("Couldn't find a master in the cluster. Setting the current node as master");
			clusterService.join(node.getMember(), true);
			node.joined();
		} else {
			JoinRequest request = new JoinRequest(node.getMember());
			Connection connection = connectionManager.getConnection(master);
			Response response = null;
			try {
				logger.info("Sending the join request " + request + " to the master - " + master);
				response = connection.send(request).get();
			} catch (InterruptedException e) {
				logger.error("Failed while getting the join response", e);
				// TODO handle exception
			}
			if (response.getStatus() == Status.success) {
				JoinResponse joinResponse = (JoinResponse) response;
				clusterService.updateClusterMembers(joinResponse.getMembers(), joinResponse.getMaster());
				node.joined();
			} else {
				logger.error("Failed while joining the cluster - " + response.getMessage());
				// TODO handle failure
			}
		}
	}
	
	protected Member findMaster(Node node) {
		logger.info("Finding the master in the cluster");
		ClusterInfo info = new ClusterInfo(true, node.getMember(), node.getCluster().getMembers().size());
		long startTime = System.currentTimeMillis();
		
		while (node.getCluster().getMaster() == null && (System.currentTimeMillis() - startTime) < 5000) {
			this.multicastService.send(info);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		return node.getCluster().getMaster();
	}
	
	@Override
	public void unjoin(Node node) {
		UnjoinRequest request = new UnjoinRequest(null);
		multicastService.send(request);
	}
}
