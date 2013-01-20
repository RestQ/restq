/**
 * 
 */
package org.restq.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.restq.cluster.impl.ClusterImpl;

/**
 * @author ganeshs
 *
 */
public class ClusterManager {
	
	private ClusterImpl cluster;
	
	private static Logger logger = Logger.getLogger(ClusterManager.class);
	
	/**
	 * @param cluster
	 */
	public ClusterManager(ClusterImpl cluster) {
		this.cluster = cluster;
	}
	
	public JoinResponse join(JoinRequest request) {
		request.getMember().getPartitions().add(getNextPartition());
		cluster.join(request.getMember());
		return new JoinResponse(cluster.getMembers(), cluster.getMaster());
	}
	
	public JoinResponse unjoin(UnjoinRequest request) {
		cluster.unjoin(request.getMember());
		return new JoinResponse(cluster.getMembers(), cluster.getMaster());
	}
	
	public Cluster getCluster() {
		return cluster;
	}
	
	public boolean updateMaster(MasterInfo info) {
		logger.info("Updating the cluster with the master info - " + info);
		if (cluster.getMaster() != null) {
			logger.info("Master already set. Rejecting the update - " +  info);
			return false;
		}
		cluster.setMaster(info.getMaster());
		return true;
	}
	
	public void updateCluster(JoinResponse response) {
		logger.info("Updating the cluster with the join response - " + response);
		
		if (cluster.getMaster() != null && ! cluster.getMaster().equals(response.getMaster())) {
			return;
		}
		cluster.setMaster(response.getMaster());
		cluster.setMembers(response.getMembers());
	}
	
	public Partition getNextPartition() {
		List<Partition> partitions = new ArrayList<Partition>();
		for(Member member : cluster.getMembers()) {
			partitions.addAll(member.getPartitions());
		}
		Collections.sort(partitions);
		int index = 0;
		if (! partitions.isEmpty()) {
			index = partitions.get(0).getIndex();
		}
		return new Partition(index + 1);
	}
}
