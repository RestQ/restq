/**
 * 
 */
package org.restq.cluster.nio;

import org.restq.cluster.Member;

/**
 * @author ganeshs
 *
 */
public interface ConnectionManager {

	Connection getConnection(Member member);
	
	void close();
	
}
