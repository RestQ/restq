/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.cluster.Request;

/**
 * @author ganeshs
 *
 */
public interface PreProcessor {

	void process(Request request);
}
