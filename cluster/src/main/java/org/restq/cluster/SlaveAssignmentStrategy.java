/**
 * 
 */
package org.restq.cluster;

/**
 * @author ganeshs
 *
 */
public interface SlaveAssignmentStrategy {

	/**
	 * <p>Assigns slave for the new member or transfers slave ownership to other nodes when a node leaves</p>
	 * 
	 * @param member the member that has joined/left
	 * @param join true if the node has joined, false if the node has left
	 */
	void strategize(Member member, boolean join);
}
