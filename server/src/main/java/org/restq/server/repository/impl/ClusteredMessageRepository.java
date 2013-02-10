/**
 * 
 */
package org.restq.server.repository.impl;

import java.io.IOException;
import java.util.List;

import org.restq.cluster.Partition;
import org.restq.cluster.PartitionStrategy;
import org.restq.core.RestQException;
import org.restq.core.Serializer;
import org.restq.journal.Journal;
import org.restq.journal.Record;
import org.restq.journal.repository.JournalRepository;
import org.restq.messaging.Destination;
import org.restq.messaging.ServerMessage;
import org.restq.messaging.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class ClusteredMessageRepository implements MessageRepository {
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private PartitionStrategy partitionStrategy;
	
	@Autowired
	private Serializer serializer;
	
	public ClusteredMessageRepository() {
	}

	/**
	 * @param journalRepository
	 * @param partitionStrategy
	 * @param serializer
	 */
	public ClusteredMessageRepository(JournalRepository journalRepository,
			PartitionStrategy partitionStrategy, Serializer serializer) {
		this.journalRepository = journalRepository;
		this.partitionStrategy = partitionStrategy;
		this.serializer = serializer;
	}

	@Override
	public ServerMessage find(Destination destination, String id) {
		return null;
	}

	@Override
	public void persist(Destination destination, ServerMessage message) {
		Journal journal = getJournal(destination, message);
		journal.appendAddRecord(getRecord(message, false));
	}
	
	protected Record getRecord(ServerMessage message, boolean delete) {
		try {
			if (delete) {
				return new Record(1, JournalRepository.MESSAGE, message.getId().toString().getBytes());
			}
			return new Record(1, JournalRepository.MESSAGE, serializer.serialize(message));
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}

	@Override
	public void delete(Destination destination, ServerMessage message) {
		Journal journal = getJournal(destination, message);
		journal.appendDeleteRecord(getRecord(message, true));
	}

	@Override
	public void refresh(Destination destination) {
		
	}

	@Override
	public List<ServerMessage> findAll(Destination destination) {
		return null;
	}
	
	protected Journal getJournal(Destination destination, ServerMessage message) {
		Partition partition = partitionStrategy.getPartition(destination.getId(), message.getGroupId());
		return journalRepository.getMessagesJournal(Integer.toString(partition.getIndex()), destination.getId());
	}

}
