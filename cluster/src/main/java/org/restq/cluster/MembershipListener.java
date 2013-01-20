/**
 * 
 */
package org.restq.cluster;

/**
 * @author ganeshs
 *
 */
public interface MembershipListener {

	void memberAdded(Cluster cluster, Member member);
	
	void memberRemoved(Cluster cluster, Member member);
}
