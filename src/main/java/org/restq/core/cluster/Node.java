/**
 * 
 */
package org.restq.core.cluster;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.restq.core.cluster.impl.ClusterImpl;
import org.restq.core.cluster.impl.MemberImpl;
import org.restq.core.cluster.impl.RequestDecoder;
import org.restq.core.cluster.impl.RequestHandler;
import org.restq.core.server.RestQComponent;

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class Node implements RestQComponent {

	private ChannelFactory factory;
	
	private ServerBootstrap bootstrap;
	
	private Member member;
	
	private Config config;
	
	private Channel channel;
	
	private ClusterManager clusterManager;
	
	private AtomicBoolean joined = new AtomicBoolean();
	
	private MulticastService multicastService;
	
	private Logger logger = Logger.getLogger(Node.class);
	
	/**
	 * @param member
	 * @throws UnknownHostException 
	 */
	public Node(Config config) throws Exception {
		this(config, new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()), new ServerBootstrap());
	}
	
	/**
	 * @param config
	 * @param factory
	 * @param bootstrap
	 * @throws Exception
	 */
	public Node(Config config, ChannelFactory factory, ServerBootstrap bootstrap) throws Exception {
		this.config = config;
		this.member = new MemberImpl(config.getUuid(), config.getClusterPort());
		this.clusterManager = new ClusterManager(new ClusterImpl(member));
		this.multicastService = new MulticastService(config, this);
		this.factory = factory;
		if (bootstrap == null) {
			bootstrap = new ServerBootstrap(factory);
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
		joinCluster();
	}
	
	@Override
	public void stop() {
		logger.info("Stopping the node");
		if (channel != null) {
			channel.close();
		}
	}
	
	protected void joinCluster() {
		logger.info("Joining the cluster");
		try {
			for (int i = 0; i < config.getMaxMulticastRetries(); i++) {
				
			}
			this.multicastService.join(new JoinRequest(member));
		} catch (IOException e) {
			logger.error("Failed while joining the cluster");
			throw new RestQException(e);
		}
	}
	
	public boolean hasJoined() {
		return joined.get();
	}
	
	public void setJoined(boolean hasJoined) {
		joined.set(hasJoined);
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
	
	public static void main(String[] args) throws Exception {
		Node node = new Node(new Config("2", 3001, 8000, "224.2.2.3", 2));
		node.start();
	}
}
