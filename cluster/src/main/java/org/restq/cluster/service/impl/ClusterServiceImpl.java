/**
 * 
 */
package org.restq.cluster.service.impl;

import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.MembershipListener;
import org.restq.cluster.Node;
import org.restq.cluster.PartitionAssignmentStrategy;
import org.restq.cluster.SlaveAssignmentStrategy;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.NotMasterException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class ClusterServiceImpl implements ClusterService, MembershipListener {
	
	@Autowired
	private Node node;
	
	@Autowired
	private PartitionAssignmentStrategy partitionAssignmentStrategy;
	
	@Autowired
	private SlaveAssignmentStrategy slaveAssignmentStrategy;
	
	private static Logger logger = Logger.getLogger(ClusterServiceImpl.class);
	
	void init() {
		node.getCluster().addMembershipListener(this);
	}

	@Override
	public Cluster join(Member member, boolean joinAsMaster) {
		boolean joined = false;
		if (! joinAsMaster) {
			if (node.isMaster()) {
				joined = node.getCluster().join(member);
			} else {
				throw new NotMasterException("Node is not the master of this cluster");
			}
		}
		if (joinAsMaster) {
			joined = node.getCluster().join(member);
			node.getCluster().setMaster(member);
		}
		if (joined) {
			partitionAssignmentStrategy.strategize(member, true);
			slaveAssignmentStrategy.strategize(member, true);
			logger.info("Member successfully joined - " + member);
		}
		return node.getCluster();
	}

	@Override
	public void updateClusterMembers(Set<Member> members, Member master) {
		logger.info("Updating the cluster members");
		node.getCluster().setMembers(members);
		node.getCluster().setMaster(master);
	}
	
	@Override
	public void memberAdded(Cluster cluster, Member member) {
		
	}
	
	@Override
	public void memberRemoved(Cluster cluster, Member member) {
		
	}
}