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

	Journal getDestinationsJournal();
	
	Journal getJournal(String id);
}
