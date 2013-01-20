/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.cluster.Node;

/**
 * @author ganeshs
 *
 */
public interface Plugin {
	
	void register(Node node);
	
	void bind(Node node);
	
	void shutdown(Node node);
}
