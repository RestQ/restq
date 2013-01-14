/**
 * 
 */
package org.restq.core.cluster.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.core.cluster.ClusterManager;
import org.restq.core.cluster.JoinRequest;
import org.restq.core.cluster.JoinResponse;
import org.restq.core.cluster.MasterInfo;
import org.restq.core.cluster.Member;
import org.restq.core.cluster.MulticastService;
import org.restq.core.cluster.Node;
import org.restq.core.cluster.impl.MulticastClusterJoiner;
import org.restq.core.server.RestQException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.mockito.Mockito.*;

/**
 * @author ganeshs
 *
 */
public class MulticastClusterJoinerTest {
	
	private Node node;
	
	private MulticastService multicastService;
	
	private MulticastClusterJoiner joiner;

	@BeforeMethod
	public void setup() {
		node = mock(Node.class);
		when(node.getMember()).thenReturn(mock(Member.class));
		multicastService = mock(MulticastService.class);
		joiner = new MulticastClusterJoiner(multicastService, 3);
	}
	
	@Test
	public void shouldJoinOnFirstTry() throws IOException {
		JoinRequest request = new JoinRequest(node.getMember());
		when(node.getClusterManager()).thenReturn(mock(ClusterManager.class));
		when(multicastService.send(request)).thenReturn(true);
		when(node.hasJoined()).thenReturn(true);
		joiner.join(node);
		verify(node, times(1)).hasJoined();
		verify(multicastService, times(1)).send(request);
	}
	
	@Test
	public void shouldJoinOnSecondRetry() throws IOException {
		JoinRequest request = new JoinRequest(node.getMember());
		when(node.getClusterManager()).thenReturn(mock(ClusterManager.class));
		when(multicastService.send(request)).thenReturn(true);
		final AtomicInteger retry = new AtomicInteger();
		doAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				if (retry.incrementAndGet() == 2) {
					return true;
				}
				return false;
			}
		}).when(node).hasJoined();
		joiner.join(node);
		verify(node, times(2)).hasJoined();
		verify(multicastService, times(2)).send(request);
	}
	
	@Test
	public void shouldBecomeMasterAfterMaxRetries() {
		JoinRequest request = new JoinRequest(node.getMember());
		MasterInfo response = new MasterInfo(node.getMember());
		when(node.getClusterManager()).thenReturn(mock(ClusterManager.class));
		when(multicastService.send(request)).thenReturn(true);
		when(multicastService.send(response)).thenReturn(true);
		when(node.hasJoined()).thenReturn(false);
		joiner.join(node);
		verify(node, times(3)).hasJoined();
		verify(multicastService, times(3)).send(request);
		verify(multicastService, times(1)).send(response);
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionOnSendRequestFailure() {
		JoinRequest request = new JoinRequest(node.getMember());
		when(node.getClusterManager()).thenReturn(mock(ClusterManager.class));
		when(multicastService.send(request)).thenThrow(new RestQException());
		joiner.join(node);
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionOnSendResponseFailure() {
		JoinRequest request = new JoinRequest(node.getMember());
		MasterInfo response = new MasterInfo(node.getMember());
		when(node.getClusterManager()).thenReturn(mock(ClusterManager.class));
		when(multicastService.send(request)).thenReturn(true);
		when(multicastService.send(response)).thenThrow(new RestQException());
		when(node.hasJoined()).thenReturn(false);
		joiner.join(node);
	}
}
