/**
 * 
 */
package org.restq.cluster;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.netty.channel.Channel;
import org.restq.cluster.Node.State;
import org.restq.cluster.pipeline.MessageObserver;
import org.restq.cluster.pipeline.Plugin;
import org.restq.cluster.pipeline.PostProcessor;
import org.restq.cluster.pipeline.PreProcessor;
import org.restq.cluster.service.ClusterJoiner;
import org.restq.core.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class NodeTest {

	private Node node;
	
	private Channel channel;
	
	private Cluster cluster;
	
	private ClusterJoiner joiner; 
	
	private Serializer serializer;

	@BeforeMethod
	public void setup() throws Exception {
		joiner = mock(ClusterJoiner.class);
		serializer = mock(Serializer.class);
		cluster = mock(Cluster.class);
		node = new Node(cluster, joiner, serializer);
		node = spy(node);
		channel = mock(Channel.class);
		doReturn(channel).when(node).bindSocket();
	}
	
	@Test
	public void shouldRegisterPlugin() {
		Plugin plugin = mock(Plugin.class);
		node.registerPlugin(plugin);
		assertTrue(node.getPlugins().contains(plugin));
	}
	
	@Test
	public void shouldBindSocket() {
		node.bind();
		verify(node).bindSocket();
	}
	
	@Test
	public void shouldJoinClusterOnBind() {
		node.bind();
		verify(joiner).join(node);
	}
	
	@Test
	public void shouldCallRegisteredPluginsOnBind() {
		Plugin plugin = mock(Plugin.class);
		node.registerPlugin(plugin);
		node.bind();
		verify(joiner).join(node);
		verify(plugin).register(node);
	}
	
	@Test
	public void shouldShutdownNode() {
		node.bind();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		node.shutdown();
		verify(joiner).unjoin(node);
		verify(channel).close();
	}
	
	@Test
	public void shouldMarkAsJoined() {
		node.joined();
		assertTrue(node.hasJoined());
		assertEquals(node.getState(), State.active);
	}
	
	@Test
	public void shouldReturnTrueIfTheNodeIsMaster() {
		Member member = node.getMember();
		when(cluster.getMaster()).thenReturn(member);
		assertTrue(node.isMaster());
	}
	
	@Test
	public void shouldReturnFalseIfTheNodeIsNotMaster() {
		when(cluster.getMaster()).thenReturn(mock(Member.class));
		assertFalse(node.isMaster());
	}
	
	@Test
	public void shouldMapRequestToAction() {
		node.map(JoinRequest.class, "test", "equals");
		assertEquals(node.getRequestMapper().getMappings().get(JoinRequest.class).getController(), "test");
		assertEquals(node.getRequestMapper().getMappings().get(JoinRequest.class).getAction().getName(), "equals");
	}
	
	@Test
	public void shouldAddPreProcessors() {
		PreProcessor processor = mock(PreProcessor.class);
		node.addPreProcessor(processor);
		assertTrue(node.getPreProcessors().contains(processor));
	}
	
	@Test
	public void shouldAddPostProcessors() {
		PostProcessor processor = mock(PostProcessor.class);
		node.addPostProcessor(processor);
		assertTrue(node.getPostProcessors().contains(processor));
	}
	
	@Test
	public void shouldAddMessageObservers() {
		MessageObserver observer = mock(MessageObserver.class);
		node.addMessageObserver(observer);
		assertTrue(node.getMessageObservers().contains(observer));
	}
	
}
