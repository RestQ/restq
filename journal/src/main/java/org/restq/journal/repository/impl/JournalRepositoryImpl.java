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
import org.springframework.util.StringUtils;

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
		Journal journal = journals.get("destinations");
		if (journal == null) {
			createDirIfNotExists(destDir);
			journal = new JournalImpl("destinations", destDir, "destinations", "conf", 1, 1024*10);
			journals.put(journal.getId(), journal);
		}
		return journal;
	}
	
	@Override
	public Journal getJournal(String id) {
		return journals.get(id);
	}

	private void createDirIfNotExists(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * @return the journalDir
	 */
	public String getJournalDir() {
		return journalDir;
	}

	/**
	 * @param journalDir the journalDir to set
	 */
	public void setJournalDir(String journalDir) {
		this.journalDir = journalDir;
	}

	@Override
	public Journal getMessagesJournal(String parentDir, String destination) {
		String id = parentDir + SEPARATOR + destination;
		Journal journal = journals.get(id);
		if (journal == null) {
			String msgDir = journalDir + SEPARATOR + MESSAGES_DIR + SEPARATOR + id;
			createDirIfNotExists(msgDir);
			journal = new JournalImpl(id, msgDir, destination, "msg", 100, 1024*1000);
			journals.put(journal.getId(), journal);
		}
		return journal;
	}
}
