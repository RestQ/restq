/**
 * 
 */
package org.restq.core.cluster.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.restq.core.cluster.Cluster;
import org.restq.core.cluster.Member;
import org.restq.core.cluster.MembershipListener;

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
	public void join(Member member) {
		if (members.contains(member)) {
			logger.info("Member - " + member + " already exists in the cluster");
			return;
		}
		if (members.isEmpty()) {
			setMaster(member);
		}
		members.add(member);
		for(MembershipListener listener : membershipListeners) {
			listener.memberAdded(this, member);
		}
	}

	@Override
	public void unjoin(Member member) {
		members.remove(member);
		if (member.equals(master)) {
			if (!members.isEmpty()) {
				setMaster(members.iterator().next());
			} else {
				setMaster(null);
			}
		}
		for(MembershipListener listener : membershipListeners) {
			listener.memberRemoved(this, member);
		}
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
		return Collections.unmodifiableSet(members);
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
		if (master != null && ! members.contains(master)) {
			members.add(master);
		}
		this.master = master;
	}
	
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
}
