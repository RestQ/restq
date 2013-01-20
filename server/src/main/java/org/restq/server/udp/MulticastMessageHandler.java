/**
 * 
 */
package org.restq.server.udp;

import org.apache.log4j.Logger;
import org.restq.cluster.Node;
import org.restq.cluster.service.MulticastService;
import org.restq.cluster.service.impl.ClusterInfo;
import org.restq.core.DataSerializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public class MulticastMessageHandler {

	@Autowired
	private Node node;
	
	@Autowired
	private MulticastService multicastService;
	
	private static Logger logger = Logger.getLogger(MulticastMessageHandler.class);
	
	public void handle(DataSerializable data) {
		if (! (data instanceof ClusterInfo)) {
			logger.info("Discarding the message - " + data);
			return;
		}
		ClusterInfo info = (ClusterInfo) data;
		
		if (info.isRequest()) {
			if (node.hasJoined() && node.isMaster()) {
				multicastService.send(new ClusterInfo(false, node.getMember(), node.getCluster().getMembers().size()));
				return;
			}
			logger.info("Discarding the message as the node is not the master");
		} else {
			if (! node.hasJoined() && node.getCluster().getMaster() == null) {
				logger.info("Setting the master from the cluster info - " + info);
				node.getCluster().setMaster(info.getMember());
				return;
			}
			logger.info("Discarding the message as the master is already set");
		}
	}
}
