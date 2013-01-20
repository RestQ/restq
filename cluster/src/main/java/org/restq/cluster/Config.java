/**
 * 
 */
package org.restq.cluster;

/**
 * @author ganeshs
 *
 */
public class Config {
	
	private String uuid;

	private int clusterPort;
	
	private int httpPort;
	
	private int multicastPort;
	
	private String multicastGroup;
	
	private int maxMulticastRetries;
	
	/**
	 * @param uuid
	 * @param clusterPort
	 */
	public Config(String uuid, int httpPort, int clusterPort, int multicastPort, String multicastGroup, int maxMulticastRetries) {
		this.uuid = uuid;
		this.httpPort = httpPort;
		this.clusterPort = clusterPort;
		this.multicastGroup = multicastGroup;
		this.multicastPort = multicastPort;
		this.maxMulticastRetries = maxMulticastRetries;
	}
	
	/**
	 * @return the clusterPort
	 */
	public int getClusterPort() {
		return clusterPort;
	}

	/**
	 * @param clusterPort the clusterPort to set
	 */
	public void setClusterPort(int clusterPort) {
		this.clusterPort = clusterPort;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the multicastPort
	 */
	public int getMulticastPort() {
		return multicastPort;
	}

	/**
	 * @param multicastPort the multicastPort to set
	 */
	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	/**
	 * @return the multicastGroup
	 */
	public String getMulticastGroup() {
		return multicastGroup;
	}

	/**
	 * @param multicastGroup the multicastGroup to set
	 */
	public void setMulticastGroup(String multicastGroup) {
		this.multicastGroup = multicastGroup;
	}

	/**
	 * @return the maxMulticastRetries
	 */
	public int getMaxMulticastRetries() {
		return maxMulticastRetries;
	}

	/**
	 * @param maxMulticastRetries the maxMulticastRetries to set
	 */
	public void setMaxMulticastRetries(int maxMulticastRetries) {
		this.maxMulticastRetries = maxMulticastRetries;
	}

	/**
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

}
