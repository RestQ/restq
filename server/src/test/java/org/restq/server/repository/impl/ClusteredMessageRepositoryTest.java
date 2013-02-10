/**
 * 
 */
package org.restq.server.repository.impl;

import org.restq.cluster.Partition;
import org.restq.cluster.PartitionStrategy;
import org.restq.core.Serializer;
import org.restq.journal.Journal;
import org.restq.journal.Record;
import org.restq.journal.repository.JournalRepository;
import org.restq.messaging.Destination;
import org.restq.messaging.Message;
import org.restq.messaging.ServerMessage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author ganeshs
 *
 */
public class ClusteredMessageRepositoryTest {

	private JournalRepository journalRepository;
	
	private PartitionStrategy partitionStrategy;
	
	private Serializer serializer;
	
	private ClusteredMessageRepository messageRepository;
	
	private ServerMessage message;
	
	private Destination destination;
	
	@BeforeMethod
	public void setup() {
		journalRepository = mock(JournalRepository.class);
		partitionStrategy = mock(PartitionStrategy.class);
		serializer = mock(Serializer.class);
		messageRepository = new ClusteredMessageRepository(journalRepository, partitionStrategy, serializer);
		message = mock(ServerMessage.class);
		when(message.getId()).thenReturn("mid1");
		when(message.getGroupId()).thenReturn("g1");
		destination = mock(Destination.class);
		when(destination.getId()).thenReturn("d1");
	}
	
	@Test
	public void shouldPersistMessageInRightPartition() {
		Partition partition = new Partition(1);
		when(partitionStrategy.getPartition(destination.getId(), message.getGroupId())).thenReturn(partition);
		Journal journal = mock(Journal.class);
		when(journalRepository.getMessagesJournal(Integer.toString(partition.getIndex()), destination.getId())).thenReturn(journal);
		messageRepository.persist(destination, message);
		verify(journal).appendAddRecord(new Record(1, JournalRepository.MESSAGE, (byte[])any()));
	}
	
	@Test
	public void shouldDeleteMessageInRightPartition() {
		Partition partition = new Partition(1);
		when(partitionStrategy.getPartition(destination.getId(), message.getGroupId())).thenReturn(partition);
		Journal journal = mock(Journal.class);
		when(journalRepository.getMessagesJournal(Integer.toString(partition.getIndex()), destination.getId())).thenReturn(journal);
		messageRepository.delete(destination, message);
		verify(journal).appendDeleteRecord(new Record(1, JournalRepository.MESSAGE, message.getId().toString().getBytes()));
	}
}
