/**
 * 
 */
package org.restq.journal.repository.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.restq.journal.Journal;
import org.restq.journal.impl.JournalImpl;
import org.restq.journal.repository.JournalRepository;

/**
 * @author ganeshs
 *
 */
public class JournalRepositoryImpl implements JournalRepository {
	
	private String journalDir;
	
	private static final String DESTINATIONS_DIR = "destinations";
	
	private static final String MESSAGES_DIR = "messages";
	
	private static final String SEPARATOR = "/";
	
	private Map<String, Journal> journals = new HashMap<String, Journal>();
	
	@Override
	public Journal getDestinationsJournal() {
		String destDir = journalDir + SEPARATOR + DESTINATIONS_DIR;
		Journal journal = journals.get(destDir);
		if (journal == null) {
			createDirIfNotExists(destDir);
			journal = new JournalImpl("destinations", destDir, "destinations", ".conf", 1, 1024*10);
			journals.put(destDir, journal);
		}
		return journal;
	}

	private void createDirIfNotExists(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
