/**
 * 
 */
package org.restq.journal;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class InternalRecord implements DataSerializable {

	private int marker;
	
	private Record record;
	
	public InternalRecord() {
	}

	/**
	 * @param marker
	 * @param record
	 */
	public InternalRecord(int marker, Record record) {
		this.marker = marker;
		this.record = record;
	}

	/**
	 * @return the marker
	 */
	public int getMarker() {
		return marker;
	}

	/**
	 * @param marker the marker to set
	 */
	public void setMarker(int marker) {
		this.marker = marker;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		marker = input.readByte();
		record = new Record();
		record.readData(input);
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeByte(marker);
		record.writeData(output);
	}
	
	public long size() {
		long length = 1; // 1 (byte marker)
		if (record != null) {
			length += record.size();
		}
		return length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InternalRecord [marker=" + marker + ", record=" + record + "]";
	}
}
