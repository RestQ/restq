/**
 * 
 */
package org.restq.cluster.service;

import java.util.Set;

import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface ClusterService {

	Cluster join(Member member, boolean joinAsMaster);
	
	void updateClusterMembers(Set<Member> members, Member master);
}
