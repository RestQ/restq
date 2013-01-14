/**
 * 
 */
package org.restq.core.cluster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.restq.core.cluster.nio.DataSerializable;
import org.restq.core.cluster.nio.Serializer;
import org.restq.core.server.RestQComponent;
import org.restq.core.server.RestQException;


/**
 * @author ganeshs
 *
 */
public class MulticastService implements Runnable, RestQComponent {
	
	private MulticastSocket socket;
	
	private DatagramPacket recevicePacket;
	
	private DatagramPacket sendPacket;
	
	private boolean running = true;
	
	private Serializer serializer;
	
	private List<MulticastServiceListener> listeners = new ArrayList<MulticastService.MulticastServiceListener>();
	
	private Logger logger = Logger.getLogger(MulticastService.class);
	
	/**
	 * @param config
	 * @throws IOException
	 */
	public MulticastService(Config config) throws IOException {
		this(config, new Serializer());
	}
	
	/**
	 * <p>Constructor creating the socket from configuration</p>
	 * 
	 * @param config
	 * @throws IOException
	 */
	public MulticastService(Config config, Serializer serializer) throws IOException {
		socket = createMulticastSocket(config);
		recevicePacket = createPacket(config, false);
		sendPacket = createPacket(config, true);
		this.serializer = serializer;
	}
	
	/**
	 * <p>Constructor taking in predefined socket</p>
	 * 
	 * @param socket
	 * @param receivePacket
	 * @param sendPacket
	 */
	public MulticastService(MulticastSocket socket, DatagramPacket receivePacket, DatagramPacket sendPacket, Serializer serializer) {
		this.socket = socket;
		this.recevicePacket = receivePacket;
		this.sendPacket = sendPacket;
		this.serializer = serializer;
	}
	
	/**
	 * <p>Creates a socket from configuration</p>
	 * 
	 * @param config
	 * @return
	 * @throws IOException
	 */
	protected MulticastSocket createMulticastSocket(Config config) throws IOException {
		MulticastSocket socket = new MulticastSocket(null);
		socket.bind(new InetSocketAddress(config.getMulticastPort()));
		socket.setReuseAddress(true);
		socket.setTimeToLive(100);
		socket.joinGroup(InetAddress.getByName(config.getMulticastGroup()));
		return socket;
	}
	
	/**
	 * <p>Creates a send or receive packet from configuration</p>
	 * 
	 * @param config
	 * @param sendPacket
	 * @return
	 * @throws IOException
	 */
	protected DatagramPacket createPacket(Config config, boolean sendPacket) throws IOException {
		byte[] buffer = new byte[1024 * 64];
		if (sendPacket) {
			return new DatagramPacket(buffer, buffer.length, InetAddress.getByName(config.getMulticastGroup()), config.getMulticastPort());
		}
		return new DatagramPacket(buffer, buffer.length);
	}

	@Override
	public void run() {
		logger.info("Starting to listen on the multicast port");
		while(running) {
			try {
				socket.receive(recevicePacket);
				DataSerializable data = serializer.deserialize(new DataInputStream(new ByteArrayInputStream(recevicePacket.getData())));
				logger.error(data);
				for (MulticastServiceListener listener : listeners) {
					listener.messageRecieved(data);
				}
			} catch (IOException e) {
				logger.error("Failed while receving the data from multicast socket", e);
			}
		}
	}
	
	@Override
	public void start() {
		logger.info("Starting the multicast socket");
		new Thread(this).start();
	}
	
	@Override
	public void stop() {
		logger.info("Stopping the multicast socket");
		running = false;
		socket.close();
	}
	
	/**
	 * <p>Sends the data to the server</p>
	 * 
	 * @param serializable
	 * @return
	 */
	public boolean send(DataSerializable serializable) {
		logger.debug("Broadcasting the data - " + serializable);
		if (!running || socket.isClosed()) {
			logger.info("Multicast server is not running");
			return false;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(new DataOutputStream(baos), serializable);
			sendPacket.setData(baos.toByteArray());
			socket.send(sendPacket);
		} catch (IOException e) {
			throw new RestQException("Failed while sending the message to server", e);
		}
		return true;
	}

	/**
	 * <p>Adds the listener</p>
	 * 
	 * @param listener
	 */
	public void addListener(MulticastServiceListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * <p>Removes the listener</p>
	 * 
	 * @param listener
	 */
	public void removeListener(MulticastServiceListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * <p>Checks if the multicast server is running</p>
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return running && socket.isBound();
	}
	
	/**
	 * @author ganeshs
	 *
	 */
	public static interface MulticastServiceListener {
		
		/**
		 * <p>Triggered when a message is received in the multicast socket</p>
		 * 
		 * @param data
		 */
		void messageRecieved(DataSerializable data);
	}
}