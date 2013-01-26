/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.core.Request;

/**
 * @author ganeshs
 *
 */
public interface PreProcessor {

	void process(Request request);
}
