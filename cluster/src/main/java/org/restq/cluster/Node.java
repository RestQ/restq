/**
 * 
 */
package org.restq.cluster;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.restq.cluster.impl.MemberImpl;
import org.restq.cluster.pipeline.ClusterPipelineFactory;
import org.restq.cluster.pipeline.DataDecoder;
import org.restq.cluster.pipeline.MessageObserver;
import org.restq.cluster.pipeline.Plugin;
import org.restq.cluster.pipeline.PostProcessor;
import org.restq.cluster.pipeline.PreProcessor;
import org.restq.cluster.pipeline.RequestHandler;
import org.restq.cluster.service.ClusterJoiner;
import org.restq.core.Request;
import org.restq.core.RestQException;
import org.restq.core.Serializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class Node implements Runnable {

	private ServerBootstrap bootstrap;
	
	private Channel channel;
	
	private String id;
	
	private Member member;
	
	@Autowired
	private Cluster cluster;
	
	@Autowired
	private ClusterJoiner joiner;
	
	@Autowired
	private Serializer serializer;
	
	private int port = 7001;
	
	private int connectTimeoutMillis = 10000;
	
	private int soLinger = -1;
	
	private boolean reuseAddress = true;
	
	private int receiveBufferSize = 262140;
	
	private List<PreProcessor> preProcessors = new ArrayList<PreProcessor>();
	
	private List<PostProcessor> postProcessors = new ArrayList<PostProcessor>();
	
	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();
	
	private List<Plugin> plugins = new ArrayList<Plugin>();
	
	private RequestMapper requestMapper = new RequestMapper();
	
	private boolean joined;
	
	private State state = State.passive;
	
	private Logger logger = Logger.getLogger(Node.class);
	
	public enum State {
		passive, sync, active
	}
	
	public Node() {
	}
	
	public Node(Cluster cluster, ClusterJoiner joiner, Serializer serializer) {
		this.cluster = cluster;
		this.joiner = joiner;
		this.serializer = serializer;
		init();
	}
	
	public void init() {
		member = new MemberImpl(id, new InetSocketAddress(port));
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());
		RequestHandler handler = new RequestHandler(serializer, requestMapper, messageObservers, preProcessors, postProcessors);
		ClusterPipelineFactory pipelineFactory = new ClusterPipelineFactory();
		pipelineFactory.setDecoder(new DataDecoder(serializer));
		pipelineFactory.setExecutionHandler(new ExecutionHandler(Executors.newCachedThreadPool()));
		pipelineFactory.addRequestHandler(handler);
		bootstrap.setPipelineFactory(pipelineFactory);
		setBootstrapOptions();
	}
	
	/**
	 * @param plugin
	 */
	public void registerPlugin(Plugin plugin) {
		plugins.add(plugin);
		plugin.register(this);
	}
	
	protected ServerBootstrap getBootstrap() {
		return bootstrap;
	}
	
	public void bind() {
		logger.info("Binding the node");
		if (channel != null && channel.isOpen()) {
			logger.warn("Channel is already open. Not starting the node again");
			return;
		}
		for(Plugin plugin : plugins) {
			plugin.bind(this);
		}
		Thread thread = new Thread(this);
		thread.start();
		joiner.join(this);
	}
	
	@Override
	public void run() {
		logger.info("Starting the node at the port - " + port);
		channel = bindSocket();
	}
	
	protected Channel bindSocket() {
		return bootstrap.bind(new InetSocketAddress(port));
	}
	
	protected void setBootstrapOptions() {
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("reuseAddress", reuseAddress);
		bootstrap.setOption("child.soLinger", soLinger);
		bootstrap.setOption("connectTimeoutMillis", connectTimeoutMillis);
		bootstrap.setOption("receiveBufferSize", receiveBufferSize);
	}
	
	public void shutdown() {
		logger.info("Stopping the node");
		for(Plugin plugin : plugins) {
			plugin.shutdown(this);
		}
		joiner.unjoin(this);
		channel.close();
		bootstrap.getFactory().releaseExternalResources();
	}
	
	public boolean hasJoined() {
		return joined;
	}
	
	public void joined() {
		joined = true;
		setState(State.active);
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @return the joiner
	 */
	public ClusterJoiner getJoiner() {
		return joiner;
	}

	/**
	 * @param joiner the joiner to set
	 */
	public void setJoiner(ClusterJoiner joiner) {
		this.joiner = joiner;
	}

	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * @return the preProcessors
	 */
	public List<PreProcessor> getPreProcessors() {
		return preProcessors;
	}

	/**
	 * @param preProcessors the preProcessors to set
	 */
	public void setPreProcessors(List<PreProcessor> preProcessors) {
		this.preProcessors = preProcessors;
	}

	/**
	 * @param processor
	 */
	public void addPreProcessor(PreProcessor processor) {
		this.preProcessors.add(processor);
	}
	
	/**
	 * @return the postProcessors
	 */
	public List<PostProcessor> getPostProcessors() {
		return postProcessors;
	}

	/**
	 * @param processor
	 */
	public void addPostProcessor(PostProcessor processor) {
		this.postProcessors.add(processor);
	}

	/**
	 * @param postProcessors the postProcessors to set
	 */
	public void setPostProcessors(List<PostProcessor> postProcessors) {
		this.postProcessors = postProcessors;
	}

	/**
	 * @return the messageObservers
	 */
	public List<MessageObserver> getMessageObservers() {
		return messageObservers;
	}

	/**
	 * @param messageObservers the messageObservers to set
	 */
	public void setMessageObservers(List<MessageObserver> messageObservers) {
		this.messageObservers = messageObservers;
	}
	
	/**
	 * @param observer
	 */
	public void addMessageObserver(MessageObserver observer) {
		this.messageObservers.add(observer);
	}
	
	/**
	 * @return the plugins
	 */
	public List<Plugin> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins the plugins to set
	 */
	public void setHooks(List<Plugin> plugins) {
		this.plugins = plugins;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the requestMapper
	 */
	public RequestMapper getRequestMapper() {
		return requestMapper;
	}

	/**
	 * @param requestMapper the requestMapper to set
	 */
	public void setRequestMapper(RequestMapper requestMapper) {
		this.requestMapper = requestMapper;
	}

	/**
	 * @return the connectTimeoutMillis
	 */
	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	/**
	 * @param connectTimeoutMillis the connectTimeoutMillis to set
	 */
	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}

	/**
	 * @return the soLinger
	 */
	public int getSoLinger() {
		return soLinger;
	}

	/**
	 * @param soLinger the soLinger to set
	 */
	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	/**
	 * @return the reuseAddress
	 */
	public boolean isReuseAddress() {
		return reuseAddress;
	}

	/**
	 * @param reuseAddress the reuseAddress to set
	 */
	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	/**
	 * @return the receiveBufferSize
	 */
	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	/**
	 * @param receiveBufferSize the receiveBufferSize to set
	 */
	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	/**
	 * @param requestClass
	 * @param controller
	 * @param action
	 */
	public void map(Class<? extends Request> requestClass, Object controller, String action) {
		Method method = BeanUtils.findDeclaredMethodWithMinimalParameters(controller.getClass(), action);
		if (method == null) {
			throw new RestQException("Could find a method with the name - " + action + " in the controller - " + controller.getClass());
		}
		requestMapper.addMapping(requestClass, controller, method);
	}
	
	/**
	 * @return
	 */
	public boolean isMaster() {
		if (cluster.getMaster() != null) {
			return member.equals(cluster.getMaster());
		}
		return false;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}
}
