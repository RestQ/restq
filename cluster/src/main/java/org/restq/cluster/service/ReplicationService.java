/**
 * 
 */
package org.restq.cluster.service;

import org.restq.cluster.Member;
import org.restq.cluster.nio.ResponseFuture;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface ReplicationService {

	ResponseFuture replicateDestinations(Member member);
	
}