/**
 * 
 */
package org.restq.journal.service;

import java.io.DataInput;

import org.springframework.stereotype.Service;

/**
 * @author ganeshs
 *
 */
@Service
public interface JournalService {

	void importJournal(String journalDir, DataInput input);
}
