/**
 * 
 */
package org.restq.cluster.service.impl;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.cluster.Cluster;
import org.restq.cluster.JoinRequest;
import org.restq.cluster.JoinResponse;
import org.restq.cluster.Member;
import org.restq.cluster.Node;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ConnectionManager;
import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.MulticastService;
import org.restq.core.Response.Status;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class MulticastClusterJoinerTest {
	
	private Node node;
	
	private Cluster cluster;	
	
	private MulticastService multicastService;
	
	private ClusterService clusterService;
	
	private ConnectionManager connectionManager;
	
	private MulticastClusterJoiner joiner;

	@BeforeMethod
	public void setup() {
		node = mock(Node.class);
		cluster = mock(Cluster.class);
		when(node.getMember()).thenReturn(mock(Member.class));
		when(node.getCluster()).thenReturn(cluster);
		multicastService = mock(MulticastService.class);
		clusterService = mock(ClusterService.class);
		connectionManager = mock(ConnectionManager.class);
		joiner = new MulticastClusterJoiner(multicastService, clusterService, connectionManager);
	}
	
	@Test
	public void shouldFindMasterOnThirdTry() {
		final AtomicInteger retry = new AtomicInteger();
		final Member master = mock(Member.class);
		doAnswer(new Answer<Member>() {
			@Override
			public Member answer(InvocationOnMock invocation) throws Throwable {
				if (retry.incrementAndGet() >= 3) {
					return master;
				}
				return null;
			}
		}).when(cluster).getMaster();
		ClusterInfo request = new ClusterInfo(true, node.getMember(), 0);
		assertEquals(joiner.findMaster(node), master);
		verify(multicastService, times(2)).send(request);
	}
	
	@Test
	public void shouldJoinAsMasterIfThereIsNoMasterInCluster() {
		joiner = spy(joiner);
		doReturn(null).when(joiner).findMaster(node);
		joiner.join(node);
		verify(clusterService).join(node.getMember(), true);
		verify(node).joined();
	}
	
	@Test
	public void shouldJoinClusterAfterFindingMaster() throws IOException, InterruptedException {
		joiner = spy(joiner);
		Member master = mock(Member.class);
		doReturn(master).when(joiner).findMaster(node);
		Connection connection = mock(Connection.class);
		JoinRequest request = new JoinRequest(node.getMember());
		ResponseFuture future = mock(ResponseFuture.class);
		JoinResponse response = mock(JoinResponse.class);
		when(response.getStatus()).thenReturn(Status.success);
		when(response.getMaster()).thenReturn(master);
		Set<Member> members = new HashSet<Member>(Arrays.asList(master));
		when(response.getMembers()).thenReturn(members);
		when(future.get()).thenReturn(response);
		when(connectionManager.getConnection(master)).thenReturn(connection);
		when(connection.send(request)).thenReturn(future);
		joiner.join(node);
		verify(node).joined();
		verify(clusterService).updateClusterMembers(members, master);
	}

	@Test
	public void shouldUnjoin() {
		joiner.unjoin(node);
	}
}
