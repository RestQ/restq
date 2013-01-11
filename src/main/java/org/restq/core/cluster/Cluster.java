/**
 * 
 */
package org.restq.core.cluster;

import java.util.Set;

/**
 * @author ganeshs
 *
 */
public interface Cluster {
	
	Member getMaster();

	void join(Member node);
	
	void unjoin(Member node);
	
	void addMembershipListener(MembershipListener nodeListener);
	
	void removeMembershipListener(MembershipListener nodeListener);
	
	Set<Member> getMembers();
}
