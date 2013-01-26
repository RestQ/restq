/**
 * 
 */
package org.restq.server.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import org.apache.log4j.Logger;
import org.restq.cluster.service.MulticastService;
import org.restq.core.DataSerializable;
import org.restq.core.RestQComponent;
import org.restq.core.RestQException;
import org.restq.core.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author ganeshs
 *
 */
@Component
public class MulticastServer implements Runnable, RestQComponent, MulticastService {
	
	private int port = 27401;
	
	private String group = "224.2.2.3";
	
	private boolean reuseAddress = true;
	
	private int receiveBufferSize = 262140;
	
	private int timeToLive = 100;
	
	private MulticastSocket socket;
	
	private DatagramPacket recevicePacket;
	
	private DatagramPacket sendPacket;
	
	private boolean started;
	
	@Autowired
	private Serializer serializer;
	
	@Autowired
	private MulticastMessageHandler messageHandler;
	
	private Logger logger = Logger.getLogger(MulticastServer.class);
	
	public void init() {
		socket = createMulticastSocket();
		recevicePacket = createPacket(false);
		sendPacket = createPacket(true);
	}
	
	/**
	 * <p>Creates a socket from configuration</p>
	 * 
	 * @return
	 */
	protected MulticastSocket createMulticastSocket() {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(null);
			socket.bind(new InetSocketAddress(port));
			socket.setReuseAddress(reuseAddress);
			socket.setTimeToLive(timeToLive);
			socket.joinGroup(InetAddress.getByName(group));
		} catch (IOException e) {
			logger.error("Failed while creating the multicast socket");
			throw new RestQException(e);
		}
		return socket;
	}
	
	/**
	 * <p>Creates a send or receive packet</p>
	 * 
	 * @param sendPacket
	 * @return
	 */
	protected DatagramPacket createPacket(boolean sendPacket) {
		byte[] buffer = new byte[receiveBufferSize];
		try {
			if (sendPacket) {
				return new DatagramPacket(buffer, buffer.length, InetAddress.getByName(group), port);
			}
		} catch (IOException e) {
			logger.error("Failed while creating the send packet", e);
			throw new RestQException(e);
		}
		return new DatagramPacket(buffer, buffer.length);
	}

	@Override
	public void run() {
		logger.info("Starting the multicast server at - " + port);
		while(started) {
			try {
				socket.receive(recevicePacket);
				DataSerializable data = serializer.deserialize(new DataInputStream(new ByteArrayInputStream(recevicePacket.getData())));
				messageHandler.handle(data);
			} catch (IOException e) {
				logger.error("Failed while receving the data from multicast socket", e);
			}
		}
	}
	
	@Override
	public void start() {
		logger.info("Starting the multicast socket");
		new Thread(this).start();
		started = true;
	}
	
	@Override
	public void stop() {
		logger.info("Stopping the multicast socket");
		socket.close();
		started = false;
	}
	
	/**
	 * <p>Sends the data to the server</p>
	 * 
	 * @param serializable
	 * @return
	 */
	public boolean send(DataSerializable serializable) {
		logger.debug("Broadcasting the data - " + serializable);
		if (!started || socket.isClosed()) {
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
	 * <p>Checks if the multicast server is running</p>
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return started;
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
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
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
	 * @return the timeToLive
	 */
	public int getTimeToLive() {
		return timeToLive;
	}

	/**
	 * @param timeToLive the timeToLive to set
	 */
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
}