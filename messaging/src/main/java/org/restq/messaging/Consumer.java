/**
 * 
 */
package org.restq.messaging;

import java.util.List;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public interface Consumer extends DataSerializable {

	String getId();
	
	String getDestination();
	
	List<Filter> getFilters();
	
}
