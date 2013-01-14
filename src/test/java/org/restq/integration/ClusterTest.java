/**
 * 
 */
package org.restq.integration;

import org.restq.core.cluster.Config;
import org.restq.core.cluster.Member;
import org.restq.core.cluster.Node;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author ganeshs
 *
 */
public class ClusterTest {
	
	@Test
	public void testMultipleNodesJoinTheCluster() throws Exception {
		Node node1 = null, node2 = null, node3 = null;
		try {
			node1 = startNode(3002);
			node2 = startNode(3003);
			node3 = startNode(3004);
			Thread.sleep(5000);
			assertEquals(node1.getClusterManager().getCluster().getMembers().size(), 3);
			assertEquals(node2.getClusterManager().getCluster().getMembers().size(), 3);
			assertEquals(node3.getClusterManager().getCluster().getMembers().size(), 3);
			Member master = node1.getClusterManager().getCluster().getMaster();
			assertEquals(node1.getClusterManager().getCluster().getMaster(), master);
			assertEquals(node2.getClusterManager().getCluster().getMaster(), master);
			assertEquals(node3.getClusterManager().getCluster().getMaster(), master);
		} finally {
			stopNode(node1);
			stopNode(node2);
			stopNode(node3);
		}
	}
	
	@Test
	public void testMasterUnjoinChoosesAnotherMaster() throws Exception {
		Node node1 = null, node2 = null, node3 = null;
		try {
			node1 = startNode(3002);
			node2 = startNode(3003);
			node3 = startNode(3004);
			
			Member master = null;
			for (int i = 0; i < 200; i++ ) {
				Thread.sleep(100);
				master = node1.getClusterManager().getCluster().getMaster();
				if (master != null) {
					break;
				}
			}
			
			Node nonMasterNode = null;
			for (Node node : new Node[]{node1, node2, node3}) {
				if (node.isMaster()) {
					stopNode(node);
				} else {
					nonMasterNode = node;
				}
			}
			Thread.sleep(5000);
			Member newMaster = nonMasterNode.getClusterManager().getCluster().getMaster();
			assertFalse(master.equals(newMaster));
		} finally {
			stopNode(node1);
			stopNode(node2);
			stopNode(node3);
		}
	}
	
	public void stopNode(Node node) {
		if (node != null) {
			node.stop();
		}
	}

	public Node startNode(int port) throws Exception {
		Config config = new Config("node-" + port, port, 3001, "224.2.2.3", 1);
		final Node node = new Node(config);
		new Thread(new Runnable() {
			@Override
			public void run() {
				node.start();
			}
		}).start();
		return node;
	}
}
