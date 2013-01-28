/**
 * 
 */
package org.restq.server.tcp;

import org.restq.cluster.JoinRequest;
import org.restq.cluster.Node;
import org.restq.cluster.pipeline.Plugin;
import org.restq.journal.ImportJournalRequest;
import org.restq.messaging.EnqueueRequest;
import org.restq.server.tcp.controller.ClusterController;
import org.restq.server.tcp.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ganeshs
 *
 */
@Component
public class RequestMappingPlugin implements Plugin {
	
	@Autowired
	private ClusterController clusterController;
	
	@Autowired
	private MessageController messageController;

	@Override
	public void register(Node node) {
		node.map(JoinRequest.class, clusterController, "join");
		node.map(ImportJournalRequest.class, clusterController, "importJournal");
		node.map(EnqueueRequest.class, messageController, "sendMessage");
	}

	@Override
	public void bind(Node node) {
	}

	@Override
	public void shutdown(Node node) {
	}
}
