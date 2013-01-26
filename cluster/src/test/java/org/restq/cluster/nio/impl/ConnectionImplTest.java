/**
 * 
 */
package org.restq.cluster.nio.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
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
	
	private ClientBootstrap clientBootstrap;
	
	private Serializer serializer;
	
	private InetSocketAddress address;
	
	@BeforeMethod
	public void setup() {
		clientBootstrap = mock(ClientBootstrap.class);
		serializer = mock(Serializer.class);
		address = new InetSocketAddress(3001);
		connectionImpl = new ConnectionImpl(address, clientBootstrap, serializer);
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
		verify(clientBootstrap, times(1)).releaseExternalResources();
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
		verify(serializer).serialize(any(DataOutput.class), eq(request));
	}
	
	private void openConnection(ChannelFuture future, Channel channel) throws InterruptedException {
		when(channel.isOpen()).thenReturn(true);
		when(future.sync()).thenReturn(future);
		when(future.isSuccess()).thenReturn(true);
		when(future.getChannel()).thenReturn(channel);
		when(clientBootstrap.connect(address)).thenReturn(future);
		connectionImpl.open();
	}
}
