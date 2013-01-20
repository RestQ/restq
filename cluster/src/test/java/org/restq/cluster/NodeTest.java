/**
 * 
 */
package org.restq.cluster;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.restq.cluster.service.ClusterJoiner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class NodeTest {

	private Node node;
	
	private ServerBootstrap bootstrap;
	
	private MulticastServer multicastService;
	
	private Member member;
	
	private ClusterManager clusterManager;
	
	private ClusterJoiner joiner; 

	@BeforeMethod
	public void setup() throws Exception {
		bootstrap = mock(ServerBootstrap.class);
		multicastService = mock(MulticastServer.class);
		member = mock(Member.class);
		clusterManager = mock(ClusterManager.class);
		when(clusterManager.getCluster()).thenReturn(mock(Cluster.class));
		joiner = mock(ClusterJoiner.class);
		node = new Node(member, clusterManager, multicastService, joiner, bootstrap);
	}
	
	@Test
	public void shouldStartNode() {
		when(member.getAddress()).thenReturn(mock(InetSocketAddress.class));
		node.start();
		verify(multicastService).start();
		verify(bootstrap).bind(member.getAddress());
		verify(joiner).join(node);
	}
	
	@Test
	public void shouldStopNode() {
		Channel channel = mock(Channel.class); 
		when(member.getAddress()).thenReturn(mock(InetSocketAddress.class));
		when(bootstrap.bind(member.getAddress())).thenReturn(channel);
		node.start();
		node.stop();
		verify(multicastService).stop();
		verify(joiner).unjoin(node);
		verify(channel).close();
	}
	
	@Test
	public void shouldRejectItsOwnJoinRequest() {
		JoinRequest request = new JoinRequest(node.getMember());
		node.messageRecieved(request);
		verify(clusterManager, never()).join(request);
	}
	
	@Test
	public void shouldRejectJoinRequestIfNodeIsNotMaster() {
		JoinRequest request = new JoinRequest(mock(Member.class));
		when(clusterManager.getCluster().getMaster()).thenReturn(mock(Member.class));
		node.messageRecieved(request);
		verify(clusterManager, never()).join(request);
	}
	
	@Test
	public void shouldAcceptJoinRequestIfNodeIsMaster() {
		JoinRequest request = new JoinRequest(mock(Member.class));
		when(clusterManager.getCluster().getMaster()).thenReturn(member);
		node.messageRecieved(request);
		verify(clusterManager).join(request);
	}
}
