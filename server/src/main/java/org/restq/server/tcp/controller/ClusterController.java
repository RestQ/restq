/**
 * 
 */
package org.restq.server.tcp.controller;

import org.restq.cluster.Cluster;
import org.restq.cluster.JoinRequest;
import org.restq.cluster.JoinResponse;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.NotMasterException;
import org.restq.core.Response;
import org.restq.core.Response.Status;
import org.restq.journal.ImportJournalRequest;
import org.restq.journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ganeshs
 *
 */
@Component
public class ClusterController {
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private JournalService journalService;
	
	public JoinResponse join(JoinRequest request) {
		JoinResponse response = null;
		try {
			Cluster cluster = clusterService.join(request.getMember(), false);
			response = new JoinResponse(cluster.getMembers(), cluster.getMaster());
			response.setStatus(Status.success);
		} catch (NotMasterException e) {
			response = new JoinResponse();
			response.setStatus(Status.failed);
			response.setMessage(e.getMessage());
		}
		return response; 
	}
	
	public Response importJournal(ImportJournalRequest request) {
		journalService.importJournal(request.getJournalId(), request.getInput());
		return new Response(Status.success);
	}
}
