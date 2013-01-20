/**
 * 
 */
package org.restq.cluster.pipeline;

import org.restq.cluster.Request;
import org.restq.cluster.Response;

/**
 * @author ganeshs
 *
 */
public interface PostProcessor {

	void process(Request request, Response response);
}
