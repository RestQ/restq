/**
 * 
 */
package org.restq.messaging.repository.impl;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.restq.core.Serializer;
import org.restq.journal.Journal;
import org.restq.journal.JournalReaderCallback;
import org.restq.journal.repository.JournalRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class DestinationRepositoryImplTest {
	
	private DestinationRepositoryImpl destinationRepository;
	
	private JournalRepository journalRepository;
	
	private Serializer serializer;
	
	@BeforeMethod
	public void setup() {
		journalRepository = mock(JournalRepository.class);
		destinationRepository = new DestinationRepositoryImpl(journalRepository, serializer);
	}

	@Test
	public void shouldGetDestinationJournalOnInit() {
		Journal journal = mock(Journal.class);
		when(journalRepository.getDestinationsJournal()).thenReturn(journal);
		destinationRepository.init();
		verify(journalRepository, atLeast(1)).getDestinationsJournal();
	}
	
	@Test
	public void shouldRegisterWithJournalListener() {
		Journal journal = mock(Journal.class);
		when(journalRepository.getDestinationsJournal()).thenReturn(journal);
		destinationRepository.init();
		verify(journal).addListener(destinationRepository);
	}
	
	@Test
	public void shouldRefreshOnInit() {
		destinationRepository = spy(destinationRepository);
		doNothing().when(destinationRepository).refresh();
		Journal journal = mock(Journal.class);
		when(journalRepository.getDestinationsJournal()).thenReturn(journal);
		destinationRepository.init();
		verify(destinationRepository).refresh();
	}
	
	@Test
	public void shouldRegisterCallbackOnRefresh() {
		Journal journal = mock(Journal.class);
		when(journalRepository.getDestinationsJournal()).thenReturn(journal);
		destinationRepository.init();
		JournalReaderCallback callback = mock(JournalReaderCallback.class);
		destinationRepository.refresh(callback);
		verify(journal).readRecords(callback);
	}
}
