/**
 * 
 */
package org.restq.cluster.service;

import org.restq.core.DataSerializable;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface MulticastService {

	boolean send(DataSerializable data);
}
