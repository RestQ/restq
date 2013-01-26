/**
 * 
 */
package org.restq.journal;

/**
 * @author ganeshs
 *
 */
public interface JournalFile {

	long getSize();
	
	void close();
	
	InternalRecord readRecord();
	
	boolean writeRecord(InternalRecord record);
	
	boolean isFull();
}
