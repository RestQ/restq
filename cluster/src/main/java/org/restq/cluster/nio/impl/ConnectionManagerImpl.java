/**
 * 
 */
package org.restq.cluster.nio.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restq.cluster.Member;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ConnectionManager;
import org.restq.cluster.nio.Serializer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class ConnectionManagerImpl implements ConnectionManager {
	
	@Autowired
	private Serializer serializer;
	
	private Map<Member, Connection> connections = new ConcurrentHashMap<Member, Connection>();

	@Override
	public Connection getConnection(Member member) {
		Connection connection = connections.get(member);
		if (connection != null && connection.isOpen()) {
			return connection;
		}
		if (connection != null) {
			connections.remove(connection);
		}
		connection = new ConnectionImpl(member.getAddress(), serializer);
		connection.open();
		connections.put(member, connection);
		return connection;
	}

	@Override
	public void close() {
		for (Connection connection : connections.values()) {
			connection.close();
		}
		connections.clear();
	}

}
