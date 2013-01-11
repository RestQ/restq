/**
 * 
 */
package org.restq.core.cluster.nio.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer;
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

import com.restq.core.server.RestQException;

/**
 * @author ganeshs
 *
 */
public class ConnectionImpl implements Connection {
	
	private InetSocketAddress address;
	
	private ClientBootstrap bootstrap;
	
	private ChannelFactory factory;
	
	private Channel channel;
	
	private boolean open;
	
	/**
	 * @param address
	 */
	public ConnectionImpl(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	public synchronized void open() {
		if (channel != null && channel.isOpen() && channel.isConnected()) {
			// TODO Log message
			return;
		}
		if (factory == null) {
			factory =  new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		}
		if (bootstrap == null) {
			bootstrap = new ClientBootstrap(factory);
		}
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ClusterChannelHandler());
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		ChannelFuture future = bootstrap.connect(address);
		try {
			if (! future.sync().isSuccess()) {
				throw new RestQException(future.getCause());
			}
		} catch (InterruptedException e) {
			// TODO handle this
			e.printStackTrace();
		}
		channel = future.getChannel();
		open = true;
	}

	@Override
	public synchronized void close() {
		if (! open) {
			// TODO Log message
			return;
		}
		if (factory != null) {
			factory.releaseExternalResources();
		}
		if (bootstrap != null) {
			bootstrap.releaseExternalResources();
		}
	}
	
	/**
	 * @return
	 */
	public Channel getChannel() {
		return channel;
	}

	@Override
	public InetSocketAddress getAddress() {
		return address;
	}
	
	@Override
	public void send(Request request) {
//		channel.write(message)
	}

	public static void main(String[] args) throws Exception {
		ConnectionImpl connection = new ConnectionImpl(new InetSocketAddress(3000));
		connection.open();
		DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
		DataOutput output = new ChannelBufferOutputStream(buffer);
		Serializer.instance().serialize(output, new MemberImpl("test123", 8080));
		connection.getChannel().write(buffer);
		Thread.sleep(1000);
		connection.getChannel().close();
	}
}
