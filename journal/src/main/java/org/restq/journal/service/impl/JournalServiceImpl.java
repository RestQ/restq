/**
 * 
 */
package org.restq.journal.service.impl;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.RestQException;
import org.restq.journal.Journal;
import org.restq.journal.repository.JournalRepository;
import org.restq.journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class JournalServiceImpl implements JournalService {
	
	@Autowired
	private JournalRepository journalRepository;

	@Override
	public void importJournal(String journalDir, DataInputWrapper input) {
		Journal journal = journalRepository.getJournal(journalDir);
		try {
			journal.readData(input);
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}

}
