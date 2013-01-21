/**
 * 
 */
package org.restq.cluster.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.MembershipListener;
import org.restq.cluster.Partition;

/**
 * @author ganeshs
 *
 */
public class ClusterImpl implements Cluster {
	
	private Set<Member> members = new HashSet<Member>();
	
	private Set<MembershipListener> membershipListeners = new HashSet<MembershipListener>();
	
	private Member master;
	
	private Logger logger = Logger.getLogger(ClusterImpl.class);
	
	public ClusterImpl() {
	}
	
	@Override
	public boolean join(Member member) {
		synchronized (members) {
			if (members.contains(member)) {
				logger.info("Member - " + member + " already exists in the cluster");
				return false;
			}
			members.add(member);
		}
		for(MembershipListener listener : membershipListeners) {
			listener.memberAdded(this, member);
		}
		return true;
	}

	@Override
	public boolean unjoin(Member member) {
		synchronized (members) {
			for (Partition partition : member.getPartitions()) {
				members.remove(partition);
			}
			if (member.equals(master)) {
				if (!members.isEmpty()) {
					// TODO announce the current node as master?? or conduct an election again
				} else {
					setMaster(null);
				}
			}
		}
		for(MembershipListener listener : membershipListeners) {
			listener.memberRemoved(this, member);
		}
		return true;
	}

	@Override
	public void addMembershipListener(MembershipListener nodeListener) {
		membershipListeners.add(nodeListener);
	}

	@Override
	public void removeMembershipListener(MembershipListener nodeListener) {
		membershipListeners.remove(nodeListener);
	}
	
	@Override
	public Set<Member> getMembers() {
		return members;
	}
	
	/**
	 * @return the master
	 */
	public Member getMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(Member master) {
		synchronized (members) {
			if (master != null && ! members.contains(master)) {
				members.add(master);
			}
			this.master = master;
		}
	}
	
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
}
