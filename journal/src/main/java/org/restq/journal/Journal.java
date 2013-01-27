/**
 * 
 */
package org.restq.journal;

import java.util.List;

import org.restq.core.DataSerializable;


/**
 * @author ganeshs
 *
 */
public interface Journal extends DataSerializable {
	
	String getId();
	
	List<JournalFile> getFiles();
	
	void appendAddRecord(Record record);
	
	void appendUpdateRecord(Record record);
	
	void appendDeleteRecord(Record record);
	
	void close();
	
	void readRecords(JournalReaderCallback callback);
	
	void addListener(JournalListener listener);
}
