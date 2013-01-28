/**
 * 
 */
package org.restq.server.udp;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.cluster.Member;
import org.restq.cluster.impl.MemberImpl;
import org.restq.core.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class MulticastServerTest {

	private MulticastServer multicastService;
	
	private MulticastMessageHandler handler;
	
	private Serializer serializer;
	
	@BeforeMethod
	public void setup() {
		handler = mock(MulticastMessageHandler.class);
		serializer = mock(Serializer.class);
		multicastService = spy(new MulticastServer(handler, serializer));
	}
	
	@Test
	public void shouldCreateMulticastSocketFromConfig() throws IOException {
		MulticastSocket socket = multicastService.createMulticastSocket();
		assertNotNull(socket);
		assertEquals(socket.getLocalPort(), MulticastServer.DEFAULT_PORT);
	}
	
	@Test
	public void shouldCreateSendPacket() throws IOException {
		DatagramPacket packet = multicastService.createPacket(true);
		assertNotNull(packet);
		assertEquals(packet.getAddress().getHostName(), MulticastServer.DEFAULT_GROUP);
		assertEquals(packet.getPort(), MulticastServer.DEFAULT_PORT);
		assertEquals(packet.getLength(), MulticastServer.DEFAULT_PACKET_SIZE);
	}
	
	@Test
	public void shouldCreateReceivedPacket() throws IOException {
		DatagramPacket packet = multicastService.createPacket(false);
		assertNotNull(packet);
		assertNull(packet.getAddress());
		assertEquals(packet.getPort(), -1);
		assertEquals(packet.getLength(), MulticastServer.DEFAULT_PACKET_SIZE);
	}
	
	@Test
	public void shouldStartServer() {
		doNothing().when(multicastService).process();
		multicastService.start();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		assertTrue(multicastService.isStarted());
	}
	
	@Test
	public void shouldStopServer() {
		MulticastSocket socket = mock(MulticastSocket.class);
		doReturn(socket).when(multicastService).createMulticastSocket();
		doNothing().when(multicastService).process();
		multicastService.init();
		multicastService.start();
		multicastService.stop();
		assertEquals(multicastService.isStarted(), false);
		verify(socket).close();
	}
	
	@Test
	public void shouldCallHandlerWhenMessagesAreReceived() throws IOException {
		MulticastSocket socket = mock(MulticastSocket.class);
		doReturn(socket).when(multicastService).createMulticastSocket();
		multicastService.init();
		Member member = new MemberImpl();
		when(serializer.deserialize(any(DataInput.class))).thenReturn(member);
		final AtomicInteger i = new AtomicInteger();
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				i.incrementAndGet();
				if (i.get() == 3) {
					multicastService.stop();
				}
				return null;
			}
		}).when(socket).receive(any(DatagramPacket.class));
		multicastService.run();
		verify(serializer, times(3)).deserialize(any(DataInput.class));
		verify(socket, times(3)).receive(any(DatagramPacket.class));
		verify(handler, times(3)).handle(member);
	}
	
	@Test
	public void shouldReturnStatusOfServer() {
		MulticastSocket socket = mock(MulticastSocket.class);
		doReturn(socket).when(multicastService).createMulticastSocket();
		multicastService.init();
		assertFalse(multicastService.isStarted());
		doNothing().when(multicastService).process();
		multicastService.start();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		assertTrue(multicastService.isStarted());
		multicastService.stop();
		assertFalse(multicastService.isStarted());
	}
	
	@Test
	public void shouldSendData() throws IOException {
		MulticastSocket socket = mock(MulticastSocket.class);
		doReturn(socket).when(multicastService).createMulticastSocket();
		multicastService.init();
		multicastService.start();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		Member member = mock(Member.class);
		multicastService.send(member);
		verify(serializer).serialize(any(DataOutput.class), eq(member));
		verify(socket).send(any(DatagramPacket.class));
	}
	
	@Test
	public void shouldNotSendDataIfServerIsNotStarted() throws IOException {
		MulticastSocket socket = mock(MulticastSocket.class);
		doReturn(socket).when(multicastService).createMulticastSocket();
		multicastService.init();
		Member member = mock(Member.class);
		assertFalse(multicastService.send(member));
	}
}
