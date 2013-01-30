/**
 * 
 */
package org.restq.journal;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.Request;
import org.restq.core.RestQException;

/**
 * @author ganeshs
 *
 */
public class ImportJournalRequest extends Request {
	
	public enum Type {
		destinations
	}

	private DataInputWrapper input;
	
	private String journalId;
	
	private Journal journal;
	
	private Type type;
	
	public ImportJournalRequest() {
	}

	/**
	 * @param member
	 * @param type
	 */
	public ImportJournalRequest(Journal journal, Type type) {
		this.journal = journal;
		this.journalId = journal.getId();
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	public void readJournal(Journal journal) {
		if (this.journal != null) {
			throw new RestQException("Journal already set");
		}
		if (input == null) {
			throw new RestQException("Nothing to read");
		}
		try {
			journal.readData(input);
		} catch (IOException e) {
			throw new RestQException(e);
		} finally {
			input = null;
		}
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		this.input = input;
		super.readData(input);
		type = Type.values()[input.readInt()];
		journalId = input.readString();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		output.writeInt(type.ordinal());
		output.writeString(journalId);
		journal.writeData(output);
	}

	/**
	 * @return the input
	 */
	public DataInputWrapper getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(DataInputWrapper input) {
		this.input = input;
	}

	/**
	 * @return the journalId
	 */
	public String getJournalId() {
		return journalId;
	}

	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(String journalId) {
		this.journalId = journalId;
	}
}
