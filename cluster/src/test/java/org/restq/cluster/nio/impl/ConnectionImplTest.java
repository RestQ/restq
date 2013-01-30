/**
 * 
 */
package org.restq.cluster.nio.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.restq.cluster.pipeline.ResponseHandler;
import org.restq.core.DataOutputWrapper;
import org.restq.core.Request;
import org.restq.core.RestQException;
import org.restq.core.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author ganeshs
 *
 */
public class ConnectionImplTest {
	
	private ConnectionImpl connectionImpl;
	
	private Serializer serializer;
	
	private ResponseHandler handler;
	
	private InetSocketAddress address;
	
	@BeforeMethod
	public void setup() {
		serializer = mock(Serializer.class);
		address = new InetSocketAddress(3001);
		handler = mock(ResponseHandler.class);
		connectionImpl = new ConnectionImpl(address, handler, serializer);
	}
	
	@Test
	public void shouldOpenConnection() throws InterruptedException {
		ChannelFuture future = mock(ChannelFuture.class);
		Channel channel = mock(Channel.class);
		openConnection(future, channel);
		assertTrue(connectionImpl.isOpen());
	}
	
	@Test
	public void shouldCloseConnection() throws InterruptedException {
		ChannelFuture future = mock(ChannelFuture.class);
		Channel channel = mock(Channel.class);
		openConnection(future, channel);
		connectionImpl.close();
		assertFalse(connectionImpl.isOpen());
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionOnSendIfConnectionIsNotOpen() {
		connectionImpl.send(mock(Request.class));
	}
	
	@Test
	public void shouldSendRequest() throws InterruptedException, IOException {
		ChannelFuture future = mock(ChannelFuture.class);
		Channel channel = mock(Channel.class);
		openConnection(future, channel);
		Request request = mock(Request.class);
		when(channel.write(any())).thenReturn(mock(ChannelFuture.class));
		connectionImpl.send(request);
		verify(serializer).serialize(any(DataOutputWrapper.class), eq(request));
	}
	
	private void openConnection(ChannelFuture future, Channel channel) throws InterruptedException {
		connectionImpl = spy(connectionImpl);
		ChannelPipeline pipeline = mock(ChannelPipeline.class);
		when(pipeline.getContext(handler)).thenReturn(mock(ChannelHandlerContext.class));
		when(channel.isOpen()).thenReturn(true);
		when(future.sync()).thenReturn(future);
		when(future.isSuccess()).thenReturn(true);
		when(future.getChannel()).thenReturn(channel);
		when(channel.getPipeline()).thenReturn(pipeline);
		doReturn(future).when(connectionImpl).connect();
		connectionImpl.open();
	}
}
