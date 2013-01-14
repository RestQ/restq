/**
 * 
 */
package org.restq.core.cluster;

import java.util.Arrays;
import java.util.HashSet;

import org.restq.core.cluster.impl.ClusterImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author ganeshs
 *
 */
public class ClusterManagerTest {

	private ClusterManager clusterManager;
	
	private ClusterImpl cluster;
	
	@BeforeMethod
	public void setup() {
		cluster = new ClusterImpl();
		clusterManager = new ClusterManager(cluster);
	}
	
	@Test
	public void shouldProcessJoinRequest() {
		Member member = mock(Member.class);
		JoinRequest request = new JoinRequest(member);
		JoinResponse response = clusterManager.join(request);
		assertEquals(response.getMembers().size(), 1);
		assertEquals(response.getMaster(), member);
		request = new JoinRequest(mock(Member.class));
		response = clusterManager.join(request);
		assertEquals(response.getMembers().size(), 2);
		assertEquals(response.getMaster(), member);
	}
	
	@Test
	public void shouldProcessUnjoinRequest() {
		Member member = mock(Member.class);
		JoinRequest request = new JoinRequest(member);
		JoinResponse response = clusterManager.join(request);
		assertEquals(response.getMembers().size(), 1);
		assertEquals(response.getMaster(), member);
		UnjoinRequest request1 = new UnjoinRequest(member);
		response = clusterManager.unjoin(request1);
		assertEquals(response.getMembers().size(), 0);
		assertNull(response.getMaster());
	}
	
	@Test
	public void shouldUpdateClusterWithMembersAndMaster() {
		Member member = mock(Member.class);
		Member master = mock(Member.class);
		clusterManager.updateCluster(new JoinResponse(new HashSet<Member>(Arrays.asList(member, master)), master));
		assertEquals(clusterManager.getCluster().getMaster(), master);
		assertEquals(clusterManager.getCluster().getMembers().size(), 2);
	}
}
