/**
 * 
 */
package org.restq.journal;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public interface JournalFile extends DataSerializable {

	long getSize();
	
	void close();
	
	InternalRecord readRecord();
	
	boolean writeRecord(InternalRecord record);
	
	boolean isFull();
}
