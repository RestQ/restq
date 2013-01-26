/**
 * 
 */
package org.restq.journal;


/**
 * @author ganeshs
 *
 */
public interface JournalReaderCallback {

	void readAddRecord(Record record);
	
	void readUpdateRecord(Record record);
	
	void readDeleteRecord(Record record);
}
