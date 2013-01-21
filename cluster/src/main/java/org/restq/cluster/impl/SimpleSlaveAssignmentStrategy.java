/**
 * 
 */
package org.restq.cluster.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.SlaveAssignmentStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class SimpleSlaveAssignmentStrategy implements SlaveAssignmentStrategy {
	
	@Autowired
	private Cluster cluster;
	
	public SimpleSlaveAssignmentStrategy() {
	}
	
	public SimpleSlaveAssignmentStrategy(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public void strategize(Member member, boolean join) {
		if (join) {
			assignSlaves(member);
		} else {
			transferOwnership(member);
		}
	}
	
	protected void assignSlaves(Member member) {
		TreeMap<Member, Integer> map = getSlaveCount();
		map.remove(member);
		Member m = map.navigableKeySet().pollLast();
		if (m != null) {
			member.addSlave(m);
		}
		// The new member should be a slave for some node.
		Member m1 = map.navigableKeySet().pollFirst();
		if (m1 == null) {
			if (m != null) {
				m.addSlave(member);
			}
		} else {
			m1.addSlave(member);
		}
	}
	
	protected TreeMap<Member, Integer> getSlaveOfCount() {
		Map<Member, Integer> slaveOfCountMap = new HashMap<Member, Integer>();
		for (Member clusterMember : cluster.getMembers()) {
			for (Member slave : clusterMember.getSlaves()) {
				Integer count = slaveOfCountMap.get(slave);
				if (count == null) {
					slaveOfCountMap.put(clusterMember, 1);
				} else {
					slaveOfCountMap.put(clusterMember, count + 1);
				}
			}
		}
		TreeMap<Member, Integer> map = new TreeMap<Member, Integer>(new SlaveCountComparator(slaveOfCountMap));
		map.putAll(slaveOfCountMap);
		return map;
	}
	
	protected TreeMap<Member, Integer> getSlaveCount() {
		Map<Member, Integer> slaveCountMap = new HashMap<Member, Integer>();
		for (Member clusterMember : cluster.getMembers()) {
			slaveCountMap.put(clusterMember, clusterMember.getSlaves().size());
		}
		TreeMap<Member, Integer> map = new TreeMap<Member, Integer>(new SlaveCountComparator(slaveCountMap));
		map.putAll(slaveCountMap);
		return map;
	}
	
	protected void transferOwnership(Member slave) {
		// TODO handle unjoin cases
	}
	
	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	private class SlaveCountComparator implements Comparator<Member> {
		
		private Map<Member, Integer> map = new HashMap<Member, Integer>();
		
		/**
		 * @param map
		 */
		public SlaveCountComparator(Map<Member, Integer> map) {
			this.map = map;
		}
		
		@Override
		public int compare(Member member1, Member member2) {
			if (member1 != null && member2 != null && member1.equals(member2)) {
				return 0;
			}
			if (member1 == null) {
				return -1;
			}
			if (member2 == null) {
				return 1;
			}
			if (map.get(member1) > map.get(member2)) {
				return 1;
			}
			return -1;
		}
	}
}
