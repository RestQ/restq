/**
 * 
 */
package org.restq.cluster;

import java.util.Set;

/**
 * @author ganeshs
 *
 */
public interface Cluster {
	
	Member getMaster();
	
	void setMaster(Member master);

	void join(Member node);
	
	void unjoin(Member node);
	
	void addMembershipListener(MembershipListener nodeListener);
	
	void removeMembershipListener(MembershipListener nodeListener);
	
	Set<Member> getMembers();
	
	void setMembers(Set<Member> members);
}
