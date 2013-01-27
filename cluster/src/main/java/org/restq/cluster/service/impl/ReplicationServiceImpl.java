/**
 * 
 */
package org.restq.cluster.service.impl;

import org.restq.cluster.Member;
import org.restq.cluster.nio.Connection;
import org.restq.cluster.nio.ConnectionManager;
import org.restq.cluster.nio.ResponseFuture;
import org.restq.cluster.service.ReplicationService;
import org.restq.journal.ImportJournalRequest;
import org.restq.journal.ImportJournalRequest.Type;
import org.restq.journal.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class ReplicationServiceImpl implements ReplicationService {
	
	@Autowired
	private ConnectionManager connectionManager;
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Override
	public ResponseFuture replicateDestinations(Member member) {
		ImportJournalRequest request = new ImportJournalRequest(journalRepository.getDestinationsJournal(), Type.destinations);
		Connection connection = connectionManager.getConnection(member);
		return connection.send(request);
	}
}
