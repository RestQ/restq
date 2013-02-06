/**
 * 
 */
package org.restq.messaging;

import java.util.List;

/**
 * @author ganeshs
 *
 */
public interface ServerMessage extends Message {

	boolean matches(List<Filter> filters);
}
