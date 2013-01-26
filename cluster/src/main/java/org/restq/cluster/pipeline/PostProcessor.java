/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.core.Request;
import org.restq.core.Response;

/**
 * @author ganeshs
 *
 */
public interface PostProcessor {

	void process(Request request, Response response);
}
