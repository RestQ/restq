/**
 * 
 */
package org.restq.core.cluster;

/**
 * @author ganeshs
 *
 */
public interface ClusterJoiner {

	void join(Node node);
	
	void joinAsMaster(Node node);
	
	void unjoin(Node node);
}