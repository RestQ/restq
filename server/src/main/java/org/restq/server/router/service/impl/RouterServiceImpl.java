/**
 * 
 */
package org.restq.server.router.service.impl;

import org.restq.cluster.Member;
import org.restq.cluster.Node;
import org.restq.cluster.Partition;
import org.restq.cluster.PartitionStrategy;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ConnectionManager;
import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.nio.ResponseFutureListener;
import org.restq.messaging.Destination;
import org.restq.messaging.EnqueueRequest;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.service.MessageService;
import org.restq.messaging.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class RouterServiceImpl implements RouterService {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private Node node;
	
	@Autowired
	private PartitionStrategy partitionStrategy;
	
	@Autowired
	private ConnectionManager connectionManager;
	
	public RouterServiceImpl() {
	}
	
	/**
	 * @param messageService
	 * @param node
	 * @param partitionStrategy
	 * @param connectionManager
	 */
	public RouterServiceImpl(MessageService messageService, Node node,
			PartitionStrategy partitionStrategy,
			ConnectionManager connectionManager) {
		this.messageService = messageService;
		this.node = node;
		this.partitionStrategy = partitionStrategy;
		this.connectionManager = connectionManager;
	}

	@Override
	public void routeMessage(Destination destination, ServerMessage message) {
		Partition partition = partitionStrategy.getPartition(destination.getId(), message.getGroupId());
		Member member = node.getCluster().getMember(partition);
		if (member.equals(node.getMember())) {
			messageService.sendMessage(destination, message);
		} else {
			sendMessageToMember(destination, message, member);
		}
	}
	
	protected void sendMessageToMember(Destination destination, ServerMessage message, Member member) {
		EnqueueRequest request = new EnqueueRequest(destination.getId(), message);
		Connection connection = connectionManager.getConnection(member);
		ResponseFuture response = connection.send(request);
		response.addListener(new ResponseFutureListener() {
			@Override
			public void completed(ResponseFuture future) {
				// TODO Handle completed
//				System.out.println("Message successfully sent");
			}
		});
	}
}
