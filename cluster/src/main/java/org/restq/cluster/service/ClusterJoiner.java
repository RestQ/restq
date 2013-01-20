/**
 * 
 */
package org.restq.cluster.service;

import org.restq.cluster.Node;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface ClusterJoiner {

	void join(Node node);
	
	void unjoin(Node node);
}
