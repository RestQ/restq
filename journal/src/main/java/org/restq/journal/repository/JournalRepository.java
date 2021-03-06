/**
 * 
 */
package org.restq.journal.repository;

import org.restq.journal.Journal;
import org.springframework.stereotype.Repository;

/**
 * @author ganeshs
 *
 */
@Repository
public interface JournalRepository {
	
	public static final byte DESTINATION = 1;
	
	public static final byte MESSAGE = 2;

	Journal getDestinationsJournal();
	
	Journal getMessagesJournal(String parentDir, String destination);
	
	Journal getJournal(String id);
}
