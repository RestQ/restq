/**
 * 
 */
package org.restq.core.cluster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.restq.core.cluster.nio.DataSerializable;
import org.restq.core.cluster.nio.Serializer;
import org.restq.core.server.RestQComponent;

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class MulticastService implements Runnable, RestQComponent, MembershipListener {
	
	private MulticastSocket socket;
	
	private Node node;
	
	private DatagramPacket recevicePacket;
	
	private DatagramPacket sendPacket;
	
	private boolean running;
	
	private int maxRetries;
	
	private List<MulticastServiceListener> listeners = new ArrayList<MulticastService.MulticastServiceListener>();
	
	private Logger logger = Logger.getLogger(MulticastService.class);
	
	/**
	 * @param config
	 * @param clusterManager
	 * @throws IOException
	 */
	public MulticastService(Config config, Node node) throws IOException {
		this.maxRetries = config.getMaxMulticastRetries();
		this.node = node;
		socket = new MulticastSocket(null);
		socket.bind(new InetSocketAddress(config.getMulticastPort()));
		socket.setReuseAddress(true);
		socket.setTimeToLive(100);
		socket.joinGroup(InetAddress.getByName(config.getMulticastGroup()));
		
		byte[] recvBuffer = new byte[1024 * 64];
		byte[] sendBuffer = new byte[1024 * 64];
		recevicePacket = new DatagramPacket(recvBuffer, recvBuffer.length);
		sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(config.getMulticastGroup()), config.getMulticastPort());
		node.getClusterManager().getCluster().addMembershipListener(this);
	}

	@Override
	public void run() {
		logger.info("Starting to listen on the multicast port");
		while(running) {
			try {
				socket.receive(recevicePacket);
				DataSerializable data = Serializer.instance().deserialize(new DataInputStream(new ByteArrayInputStream(recevicePacket.getData())));
				for (MulticastServiceListener listener : listeners) {
					listener.messageRecieved(data);
				}
//				if (data instanceof JoinRequest) {
//					logger.info("Received a join request - " + data);
//					JoinRequest request = (JoinRequest) data;
//					
//					if (request.getMember().equals(node.getMember())) {
//						logger.info("Discarding the request as its from this node");
//					} else {
//						JoinResponse response = node.getClusterManager().join(request);
//						send(response);
//					}
//				} else if (data instanceof JoinResponse) {
//					logger.info("Received a join response - " + data);
//					node.getClusterManager().updateCluster((JoinResponse) data);
//				}
			} catch (IOException e) {
				logger.error("Failed while receving the data from multicast socket", e);
			}
		}
	}
	
	@Override
	public void start() {
		logger.info("Starting the multicast socket");
		running = true;
		new Thread(this).start();
	}
	
	@Override
	public void stop() {
		logger.info("Stopping the multicast socket");
		running = false;
		socket.close();
	}
	
	public void join(JoinRequest request) throws IOException {
		if (! running) {
			return;
		}
		for (int i = 0; i < maxRetries; i++) {
			send(request);
			if (node.hasJoined()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		logger.info("Couldn't find a master in the cluster. Assuming myself as a master");
		node.getClusterManager().join(request);
	}
	
	public void send(DataSerializable serializable) throws IOException {
		logger.debug("Broadcasting the data - " + serializable);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Serializer.instance().serialize(new DataOutputStream(baos), serializable);
		sendPacket.setData(baos.toByteArray());
		socket.send(sendPacket);
	}

	@Override
	public void memberAdded(Cluster cluster, Member member) {
		logger.debug("A new member - " + member + " has joined the cluster. Publishing to the members in the cluster");
		if (member.equals(node.getMember())) {
			node.setJoined(true);
		}
		publishJoinResponse(cluster, member);
	}
	
	@Override
	public void memberRemoved(Cluster cluster, Member member) {
		logger.debug("Member - " + member + " has left the cluster. Publishing to the members in the cluster");
		publishJoinResponse(cluster, member);
	}
	
	public void addListener(MulticastServiceListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(MulticastServiceListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void publishJoinResponse(Cluster cluster, Member member) {
		try {
			send(new JoinResponse(cluster.getMembers(), cluster.getMaster()));
		} catch (IOException e) {
			throw new RestQException("Failed while pushing the joing response");
		}
	}
	
	public static interface MulticastServiceListener {
		
		void messageRecieved(DataSerializable data);
	}
}