/**
 * 
 */
package org.restq.cluster.nio.impl;

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
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.restq.cluster.impl.MemberImpl;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.pipeline.DataDecoder;
import org.restq.cluster.pipeline.ResponseHandler;
import org.restq.core.Request;
import org.restq.core.RestQException;
import org.restq.core.Serializer;


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
	
	private ChannelHandler channelHandler;
	
	private static Logger logger = Logger.getLogger(ConnectionImpl.class);
	
	/**
	 * @param address
	 * @param channelHandler
	 * @param serializer
	 */
	public ConnectionImpl(InetSocketAddress address, final ResponseHandler channelHandler, Serializer serializer) {
		this.address = address;
		this.channelHandler = channelHandler;
		bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new DataDecoder(ConnectionImpl.this.serializer));
				pipeline.addLast("handler", channelHandler);
				return pipeline;
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		this.serializer = serializer;
	}
	
	/**
	 * @param address
	 * @param serializer
	 */
	public ConnectionImpl(InetSocketAddress address, Serializer serializer) {
		this(address, new ResponseHandler(), serializer);
	}

	@Override
	public synchronized void open() {
		if (channel != null && channel.isOpen() && channel.isConnected()) {
			logger.info("Connection is already open");
			return;
		}
		
		ChannelFuture future = connect();
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
	
	protected ChannelFuture connect() {
		return bootstrap.connect(address);
	}
	
	protected ClientBootstrap getBootstrap() {
		return bootstrap;
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
	public ResponseFuture send(Request request) {
		if (! isOpen()) {
			logger.error("Connection is not open");
			throw new RestQException("Connection is not open");
		}
		DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
		DataOutput output = new ChannelBufferOutputStream(buffer);
		ResponseFutureImpl future = new ResponseFutureImpl();
		try {
			serializer.serialize(output, request);
			ChannelHandlerContext context = channel.getPipeline().getContext(channelHandler);
			context.setAttachment(future);
			channel.write(buffer);
		} catch (IOException e) {
			logger.error("Failed while sending the message", e);
			throw new RestQException("Failed while sending the message", e);
		}
		return future;
	}

	public static void main(String[] args) throws Exception {
		ConnectionImpl connection = new ConnectionImpl(new InetSocketAddress(3000), new Serializer());
		connection.open();
		DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
		DataOutput output = new ChannelBufferOutputStream(buffer);
		new Serializer().serialize(output, new MemberImpl("test123", 8080));
		connection.channel.write(buffer);
		Thread.sleep(1000);
		connection.close();
	}
}
