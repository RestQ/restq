/**
 * 
 */
package org.restq.cluster.controller;

import org.restq.cluster.Cluster;
import org.restq.cluster.JoinRequest;
import org.restq.cluster.JoinResponse;
import org.restq.cluster.Response.Status;
import org.restq.cluster.service.ClusterService;
import org.restq.cluster.service.NotMasterException;
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
}
