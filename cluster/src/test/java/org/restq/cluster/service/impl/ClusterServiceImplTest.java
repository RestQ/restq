/**
 * 
 */
package org.restq.cluster.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.restq.cluster.Cluster;
import org.restq.cluster.Member;
import org.restq.cluster.Node;
import org.restq.cluster.PartitionAssignmentStrategy;
import org.restq.cluster.SlaveAssignmentStrategy;
import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.service.NotMasterException;
import org.restq.cluster.service.ReplicationService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class ClusterServiceImplTest {

	private ClusterServiceImpl clusterService;
	
	private Node node;
	
	private Cluster cluster;
	
	private PartitionAssignmentStrategy partitionAssignmentStrategy;
	
	private SlaveAssignmentStrategy slaveAssignmentStrategy;
	
	private ReplicationService replicationService;
	
	private Member member;
	
	@BeforeMethod
	public void setup() {
		node = mock(Node.class);
		cluster = mock(Cluster.class);
		member = mock(Member.class);
		when(node.getCluster()).thenReturn(cluster);
		when(node.getMember()).thenReturn(member);
		partitionAssignmentStrategy = mock(PartitionAssignmentStrategy.class);
		slaveAssignmentStrategy = mock(SlaveAssignmentStrategy.class);
		replicationService = mock(ReplicationService.class);
		clusterService = new ClusterServiceImpl(node, partitionAssignmentStrategy, slaveAssignmentStrategy, replicationService);
	}
	
	@Test
	public void shouldRegisterMembershipListenerOnInit() {
		clusterService.init();
		verify(cluster).addMembershipListener(clusterService);
	}
	
	@Test
	public void shouldJoinAsMaster() {
		when(cluster.join(member)).thenReturn(true);
		Cluster cluster = clusterService.join(member, true);
		assertEquals(cluster, this.cluster);
		verify(cluster).setMaster(member);
		verify(cluster).join(member);
		verify(partitionAssignmentStrategy).strategize(member, true);
		verify(slaveAssignmentStrategy).strategize(member, true);
	}
	
	@Test(expectedExceptions=NotMasterException.class)
	public void shouldNotJoinIfNodeIsNotMaster() {
		clusterService.join(member, false);
	}
	
	@Test
	public void shouldNotJoinIfAlreadyJoined() {
		when(cluster.join(member)).thenReturn(false);
		Cluster cluster = clusterService.join(member, true);
		assertEquals(cluster, this.cluster);
		verify(cluster).join(member);
		verify(partitionAssignmentStrategy, never()).strategize(member, true);
		verify(slaveAssignmentStrategy, never()).strategize(member, true);
	}
	
	@Test
	public void shouldJoinIfNodeIsMaster() {
		when(cluster.join(member)).thenReturn(true);
		when(node.isMaster()).thenReturn(true);
		Cluster cluster = clusterService.join(member, false);
		assertEquals(cluster, this.cluster);
		verify(cluster, never()).setMaster(member);
		verify(cluster).join(member);
		verify(partitionAssignmentStrategy).strategize(member, true);
		verify(slaveAssignmentStrategy).strategize(member, true);
	}
	
	@Test
	public void shouldUpdateClusterWithMembersAndMaster() {
		Member master = mock(Member.class);
		Set<Member> members = new HashSet<Member>(Arrays.asList(member, master));
		clusterService.updateClusterMembers(members, master);
		verify(cluster).setMaster(master);
		verify(cluster).setMembers(members);
	}
	
	@Test
	public void shouldHandleMemberAddedNotification() {
		Member newMember = mock(Member.class);
		clusterService = spy(clusterService);
		when(replicationService.replicateDestinations(newMember)).thenReturn(mock(ResponseFuture.class));
		clusterService.memberAdded(cluster, newMember);
		verify(clusterService).replicateJournals(newMember);
	}
	
	@Test
	public void shouldHandleMemberAddedNotificationIfItsCurrentNode() {
		clusterService = spy(clusterService);
		clusterService.memberAdded(cluster, member);
		verify(clusterService, never()).replicateJournals(member);
	}
	
	@Test
	public void shouldHandleMemberRemovedNotification() {
		clusterService.memberRemoved(cluster, member);
	}
	
	@Test
	public void shouldReplicateJournals() {
		when(replicationService.replicateDestinations(member)).thenReturn(mock(ResponseFuture.class));
		clusterService.replicateJournals(member);
		verify(replicationService).replicateDestinations(member);
	}
}
