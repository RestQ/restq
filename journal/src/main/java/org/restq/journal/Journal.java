/**
 * 
 */
package org.restq.journal;

import java.util.List;


/**
 * @author ganeshs
 *
 */
public interface Journal {
	
	String getId();
	
	List<JournalFile> getFiles();
	
	void appendAddRecord(Record record);
	
	void appendUpdateRecord(Record record);
	
	void appendDeleteRecord(Record record);
	
	void close();
	
	void readRecords(JournalReaderCallback callback);
}
