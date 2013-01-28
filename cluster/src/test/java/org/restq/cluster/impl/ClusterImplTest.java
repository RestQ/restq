/**
 * 
 */
package org.restq.cluster.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.restq.cluster.Member;
import org.restq.cluster.MembershipListener;
import org.restq.cluster.Partition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class ClusterImplTest {
	
	private ClusterImpl cluster;
	
	@BeforeMethod
	public void setup() {
		cluster = new ClusterImpl();
	}
	
	@Test
	public void shouldJoinNewMember() {
		assertTrue(cluster.join(mock(Member.class)));
		assertTrue(cluster.join(mock(Member.class)));
		assertEquals(cluster.getMembers().size(), 2);
	}
	
	@Test
	public void shouldSetMaster() {
		Member master = mock(Member.class);
		cluster.setMaster(master);
		assertEquals(cluster.getMaster(), master);
	}
	
	@Test
	public void shouldGetPartitions() {
		Member member1 = mock(Member.class);
		Member member2 = mock(Member.class);
		Set<Partition> partitions = new HashSet<Partition>(Arrays.asList(mock(Partition.class)));
		when(member1.getPartitions()).thenReturn(partitions);
		partitions = new HashSet<Partition>(Arrays.asList(mock(Partition.class)));
		when(member2.getPartitions()).thenReturn(partitions);
		cluster.join(member1);
		cluster.join(member2);
		assertEquals(cluster.getPartitions(), 2);
	}
	
	@Test
	public void shouldSetMasterAsNullOnUnjoin() {
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.setMaster(member);
		cluster.unjoin(member);
		assertEquals(cluster.getMembers().size(), 0);
		assertEquals(cluster.getMaster(), null);
	}
	
	@Test
	public void shouldRejectDuplicateJoin() {
		Member member = mock(Member.class);
		assertTrue(cluster.join(member));
		assertFalse(cluster.join(member));
		assertEquals(cluster.getMembers().size(), 1);
	}
	
	@Test
	public void shouldGetMembers() {
		Member member1 = mock(Member.class);
		Member member2 = mock(Member.class);
		cluster.join(member1);
		cluster.join(member2);
		assertEquals(cluster.getMembers(), new HashSet<Member>(Arrays.asList(member1, member2)));
	}
	
	@Test
	public void shouldGetMemberForPartition() {
		Member member1 = mock(Member.class);
		Member member2 = mock(Member.class);
		Partition partition1 = mock(Partition.class);
		Partition partition2 = mock(Partition.class);
		Set<Partition> partitions = new HashSet<Partition>(Arrays.asList(partition1));
		when(member1.getPartitions()).thenReturn(partitions);
		partitions = new HashSet<Partition>(Arrays.asList(partition2));
		when(member2.getPartitions()).thenReturn(partitions);
		cluster.join(member1);
		cluster.join(member2);
		assertEquals(cluster.getMember(partition1), member1);
		assertEquals(cluster.getMember(partition2), member2);
	}
	
	@Test
	public void shouldUnjoinMember() {
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.unjoin(member);
		assertEquals(cluster.getMembers().size(), 0);
	}
	
	@Test
	public void shouldGetMaster() {
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.setMaster(member);
		assertEquals(cluster.getMaster(), member);
	}
	
	@Test
	public void shouldNotifyOnMemberAddition() {
		MembershipListener listener = mock(MembershipListener.class);
		cluster.addMembershipListener(listener);
		Member member = mock(Member.class);
		cluster.join(member);
		verify(listener).memberAdded(cluster, member);
	}
	
	@Test
	public void shouldNotifyOnMemberRemoved() {
		MembershipListener listener = mock(MembershipListener.class);
		cluster.addMembershipListener(listener);
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.unjoin(member);
		verify(listener).memberRemoved(cluster, member);
	}
}
