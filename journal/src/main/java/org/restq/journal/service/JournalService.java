/**
 * 
 */
package org.restq.journal.service;

import org.restq.core.DataInputWrapper;
import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface JournalService {

	void importJournal(String journalDir, DataInputWrapper input);
}
