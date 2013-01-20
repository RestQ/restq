/**
 * 
 */
package org.restq.cluster.service.impl;

import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.Node;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.NotMasterException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class ClusterServiceImpl implements ClusterService {
	
	@Autowired
	private Node node;
	
	private static Logger logger = Logger.getLogger(ClusterServiceImpl.class);

	@Override
	public Cluster join(Member member, boolean joinAsMaster) {
		if (! joinAsMaster) {
			if (node.isMaster()) {
				node.getCluster().join(member);
			} else {
				throw new NotMasterException("Node is not the master of this cluster");
			}
		}
		if (joinAsMaster) {
			node.getCluster().join(member);
			node.getCluster().setMaster(member);
		}
		return node.getCluster();
	}

	@Override
	public void updateClusterMembers(Set<Member> members, Member master) {
		logger.info("Updating the cluster members");
		node.getCluster().setMembers(members);
		node.getCluster().setMaster(master);
	}
}
