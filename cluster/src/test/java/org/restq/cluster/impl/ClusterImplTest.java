/**
 * 
 */
package org.restq.cluster.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.restq.cluster.Member;
import org.restq.cluster.MembershipListener;
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
		cluster.join(mock(Member.class));
		cluster.join(mock(Member.class));
		assertEquals(cluster.getMembers().size(), 2);
	}
	
	@Test
	public void shouldJoinTheFirstMemberAsMaster() {
		Member member = mock(Member.class);
		cluster.join(member);
		assertEquals(cluster.getMaster(), member);
	}
	
	@Test
	public void shouldSetMasterAsNullOnLastUnjoin() {
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.unjoin(member);
		assertEquals(cluster.getMaster(), null);
	}
	
	@Test
	public void shouldResetMasterOnUnjoin() {
		Member member = mock(Member.class);
		Member member1 = mock(Member.class);
		cluster.join(member);
		cluster.join(member1);
		cluster.unjoin(member);
		assertEquals(cluster.getMaster(), member1);
	}
	
	@Test
	public void shouldRejectDuplicateJoin() {
		Member member = mock(Member.class);
		cluster.join(member);
		cluster.join(member);
		assertEquals(cluster.getMembers().size(), 1);
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
