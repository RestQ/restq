/**
 * 
 */
package org.restq.core.cluster.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	
	private Member thisMember;
	
	private Member master;
	
	public ClusterImpl(Member thisMember) {
		this.thisMember = thisMember;
	}

	@Override
	public void join(Member member) {
		if (members.contains(member)) {
			return;
		}
		addMember(member);
	}

	@Override
	public void unjoin(Member member) {
		removeMember(member);
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
	
	public void addMember(Member member) {
		if (members.isEmpty()) {
			setMaster(member);
		}
		members.add(member);
		for(MembershipListener listener : membershipListeners) {
			listener.memberAdded(this, member);
		}
	}
	
	public void removeMember(Member member) {
		members.remove(member);
		for(MembershipListener listener : membershipListeners) {
			listener.memberRemoved(this, member);
		}
	}

	/**
	 * @return the thisMember
	 */
	public Member getThisMember() {
		return thisMember;
	}

	/**
	 * @param thisMember the thisMember to set
	 */
	public void setThisMember(Member thisMember) {
		this.thisMember = thisMember;
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
		this.master = master;
	}
	
	public void setMembers(Set<Member> members) {
		
	}
}
