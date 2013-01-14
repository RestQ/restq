/**
 * 
 */
package org.restq.core.cluster.nio.impl;

import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.restq.core.cluster.Request;
import org.restq.core.cluster.impl.MemberImpl;
import org.restq.core.cluster.nio.ClusterChannelHandler;
import org.restq.core.cluster.nio.Connection;
import org.restq.core.cluster.nio.Serializer;
import org.restq.core.server.RestQException;


/**
 * @author ganeshs
 *
 */
public class ConnectionImpl implements Connection {
	
	private InetSocketAddress address;
	
	private static ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	
	private ClientBootstrap bootstrap = new ClientBootstrap(factory);
	
	private Channel channel;
	
	private boolean open;
	
	private Serializer serializer;
	
	private static Logger logger = Logger.getLogger(ConnectionImpl.class);
	
	/**
	 * @param address
	 * @param clientBootstrap
	 * @param serializer
	 */
	public ConnectionImpl(InetSocketAddress address, ClientBootstrap clientBootstrap, Serializer serializer) {
		this.address = address;
		this.bootstrap = clientBootstrap;
		this.serializer = serializer;
	}
	
	/**
	 * @param address
	 * @param clientBootstrap
	 */
	public ConnectionImpl(InetSocketAddress address, ClientBootstrap clientBootstrap) {
		this(address, clientBootstrap, new Serializer());
	}
	
	/**
	 * @param address
	 * @param channelHandler
	 * @param serializer
	 */
	public ConnectionImpl(InetSocketAddress address, final ClusterChannelHandler channelHandler, Serializer serializer) {
		this.address = address;
		bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(channelHandler);
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		this.serializer = serializer;
	}
	
	/**
	 * @param address
	 */
	public ConnectionImpl(InetSocketAddress address) {
		this(address, new ClusterChannelHandler(), new Serializer());
	}

	@Override
	public synchronized void open() {
		if (channel != null && channel.isOpen() && channel.isConnected()) {
			logger.info("Connection is already open");
			return;
		}
		
		ChannelFuture future = bootstrap.connect(address);
		try {
			if (! future.sync().isSuccess()) {
				throw new RestQException(future.getCause());
			}
		} catch (InterruptedException e) {
			throw new RestQException("Unable to connect to the address - " + address, e);
		}
		channel = future.getChannel();
		open = true;
	}
	
	/**
	 * @return
	 */
	public boolean isOpen() {
		return open && channel != null && channel.isOpen();
	}

	@Override
	public synchronized void close() {
		if (! open) {
			logger.info("Connection is not open");
			return;
		}
		if (bootstrap != null) {
			bootstrap.releaseExternalResources();
		}
		open = false;
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return address;
	}
	
	@Override
	public void send(Request request) {
		if (! isOpen()) {
			logger.error("Connection is not open");
			throw new RestQException("Connection is not open");
		}
		DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
		DataOutput output = new ChannelBufferOutputStream(buffer);
		try {
			serializer.serialize(output, request);
			channel.write(buffer);
		} catch (IOException e) {
			logger.error("Failed while sending the message", e);
			throw new RestQException("Failed while sending the message", e);
		}
	}

	public static void main(String[] args) throws Exception {
		ConnectionImpl connection = new ConnectionImpl(new InetSocketAddress(3000));
		connection.open();
		DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
		DataOutput output = new ChannelBufferOutputStream(buffer);
		new Serializer().serialize(output, new MemberImpl("test123", 8080));
		connection.channel.write(buffer);
		Thread.sleep(1000);
		connection.close();
	}
}