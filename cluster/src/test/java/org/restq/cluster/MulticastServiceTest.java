/**
 * 
 */
package org.restq.cluster;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
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
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.restq.cluster.impl.MemberImpl;
import org.restq.cluster.nio.Serializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class MulticastServiceTest {

	private MulticastServer multicastService;
	
	private MulticastSocket socket;
	
	private DatagramPacket sendPacket;
	
	private DatagramPacket receivePacket;
	
	private Serializer serializer;
	
	@BeforeMethod
	public void setup() {
		socket = mock(MulticastSocket.class);
		byte[] buf = new byte[1000];
		sendPacket = new DatagramPacket(buf, buf.length);
		receivePacket = new DatagramPacket(buf, buf.length);
		serializer = mock(Serializer.class);
		multicastService = spy(new MulticastServer(socket, receivePacket, sendPacket, serializer));
	}
	
	@Test
	public void shouldCreateMulticastSocketFromConfig() throws IOException {
		Config config = new Config("1", 2999, 3000, 3001, "224.2.2.3", 3);
		MulticastSocket socket = multicastService.createMulticastSocket(config);
		assertNotNull(socket);
		assertEquals(socket.getLocalPort(), config.getMulticastPort());
	}
	
	@Test
	public void shouldCreateSendPacket() throws IOException {
		Config config = new Config("1", 2999, 3000, 3001, "224.2.2.3", 3);
		DatagramPacket packet = multicastService.createPacket(config, true);
		assertNotNull(packet);
		assertEquals(packet.getAddress().getHostName(), config.getMulticastGroup());
		assertEquals(packet.getPort(), config.getMulticastPort());
		assertEquals(packet.getLength(), 64*1024);
	}
	
	@Test
	public void shouldCreateReceivedPacket() throws IOException {
		Config config = new Config("1", 2999, 3000, 3001, "224.2.2.3", 3);
		DatagramPacket packet = multicastService.createPacket(config, false);
		assertNotNull(packet);
		assertNull(packet.getAddress());
		assertEquals(packet.getPort(), -1);
		assertEquals(packet.getLength(), 64*1024);
	}
	
	@Test
	public void shouldStartServer() {
		doNothing().when(multicastService).run();
		multicastService.start();
		when(socket.isBound()).thenReturn(true);
		assertEquals(multicastService.isRunning(), true);
	}
	
	@Test
	public void shouldStopServer() {
		multicastService.stop();
		when(socket.isBound()).thenReturn(false);
		assertEquals(multicastService.isRunning(), false);
		verify(socket).close();
	}
	
	@Test
	public void shouldNotifyWhenMessagesAreReceived() throws IOException {
		MulticastServiceListener listener = mock(MulticastServiceListener.class);
		multicastService.addListener(listener);
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
		}).when(socket).receive(receivePacket);
		multicastService.run();
		verify(serializer, times(3)).deserialize(any(DataInput.class));
		verify(socket, times(3)).receive(receivePacket);
		verify(listener, times(3)).messageRecieved(member);
	}
	
	@Test
	public void shouldReturnStatusOfServer() {
		assertFalse(multicastService.isRunning());
		doNothing().when(multicastService).run();
		multicastService.start();
		when(socket.isBound()).thenReturn(true);
		assertTrue(multicastService.isRunning());
		multicastService.stop();
		assertFalse(multicastService.isRunning());
	}
}
