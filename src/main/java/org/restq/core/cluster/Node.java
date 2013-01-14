/**
 * 
 */
package org.restq.core.cluster;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.restq.core.cluster.MulticastService.MulticastServiceListener;
import org.restq.core.cluster.impl.ClusterImpl;
import org.restq.core.cluster.impl.MemberImpl;
import org.restq.core.cluster.impl.MulticastClusterJoiner;
import org.restq.core.cluster.impl.RequestDecoder;
import org.restq.core.cluster.impl.RequestHandler;
import org.restq.core.cluster.nio.DataSerializable;
import org.restq.core.cluster.nio.Serializer;
import org.restq.core.server.RestQComponent;

/**
 * @author ganeshs
 *
 */
public class Node implements RestQComponent, MembershipListener, MulticastServiceListener {

	private ServerBootstrap bootstrap;
	
	private Member member;
	
	private Channel channel;
	
	private ClusterManager clusterManager;
	
	private boolean joined;
	
	private MulticastService multicastService;
	
	private ClusterJoiner joiner;
	
	private Logger logger = Logger.getLogger(Node.class);
	
	/**
	 * @param member
	 * @throws UnknownHostException 
	 */
	public Node(Config config) throws Exception {
		this(config, new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool())));
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("executionHandler", new ExecutionHandler(Executors.newCachedThreadPool()));
				pipeline.addLast("decoder", new RequestDecoder());
				pipeline.addLast("requestHandler", new RequestHandler(clusterManager));
				return pipeline;
			}
		});
	}
	
	/**
	 * @param member
	 * @param clusterManager
	 * @param multicastService
	 * @param joiner
	 * @param bootstrap
	 */
	public Node(Member member, ClusterManager clusterManager, MulticastService multicastService, ClusterJoiner joiner, ServerBootstrap bootstrap) {
		this.member = member;
		this.clusterManager = clusterManager;
		this.multicastService = multicastService;
		this.multicastService.addListener(this);
		this.joiner = joiner;
		this.bootstrap = bootstrap;
	}
	
	/**
	 * @param config
	 * @param factory
	 * @param bootstrap
	 * @throws Exception
	 */
	public Node(Config config, ServerBootstrap bootstrap) throws Exception {
		this.member = new MemberImpl(config.getUuid(), config.getClusterPort());
		ClusterImpl cluster = new ClusterImpl();
		cluster.addMembershipListener(this);
		this.clusterManager = new ClusterManager(cluster);
		this.multicastService = new MulticastService(config, new Serializer());
		this.multicastService.addListener(this);
		this.joiner = new MulticastClusterJoiner(this.multicastService, config.getMaxMulticastRetries());
		this.bootstrap = bootstrap;
	}
	
	@Override
	public void start() {
		logger.info("Starting the node");
		if (channel != null && channel.isOpen()) {
			logger.warn("Channel is already open. Not starting the node again");
			return;
		}
		channel = bootstrap.bind(member.getAddress());
		this.multicastService.start();
		this.joiner.join(this);
	}
	
	@Override
	public void stop() {
		logger.info("Stopping the node");
		if (channel != null) {
			channel.close();
		}
		this.joiner.unjoin(this);
		this.multicastService.stop();
	}
	
	public boolean hasJoined() {
		return joined;
	}
	
	public boolean isMaster() {
		return member.equals(clusterManager.getCluster().getMaster());
	}
	
	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}
	
	@Override
	public void memberAdded(Cluster cluster, Member member) {
		logger.debug("A new member - " + member + " has joined the cluster. Publishing to the members in the cluster");
		if (this.member.equals(member)) {
			joined = true;
		}
		multicastService.send(new JoinResponse(cluster.getMembers(), cluster.getMaster()));
	}
	
	@Override
	public void memberRemoved(Cluster cluster, Member member) {
		logger.debug("Member - " + member + " has left the cluster. Publishing to the members in the cluster");
		multicastService.send(new JoinResponse(cluster.getMembers(), cluster.getMaster()));
	}
	
	@Override
	public void messageRecieved(DataSerializable data) {
		if (data instanceof MasterInfo) {
			MasterInfo info = (MasterInfo) data;
			if (clusterManager.updateMaster(info)) {
				joiner.join(this);
			}
		} else if (data instanceof JoinRequest) {
			logger.info("Received a join request - " + data);
			JoinRequest request = (JoinRequest) data;
			
			if (request.getMember().equals(member)) {
				logger.info("Discarding the request as its from this node");
			} else if (! isMaster()) {
				logger.info("Discarding the request node is not the master");
			} else {
				JoinResponse response = clusterManager.join(request);
				multicastService.send(response);
			}
		} else if (data instanceof UnjoinRequest) {
			JoinResponse response = clusterManager.unjoin((UnjoinRequest) data);
			if (response.getMaster() == null) {
				joiner.joinAsMaster(this);
			} else {
				multicastService.send(response);
			}
		} else if (data instanceof JoinResponse) {
			logger.info("Received a join response - " + data);
			clusterManager.updateCluster((JoinResponse) data);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Node node = new Node(new Config("2", 3001, 8000, "224.2.2.3", 2));
		node.start();
	}
}
