/**
 * 
 */
package org.restq.cluster;

/**
 * @author ganeshs
 *
 */
public interface PartitionAssignmentStrategy {

	void strategize(Member member, boolean join);
}
