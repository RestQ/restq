/**
 * 
 */
package org.restq.journal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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

	private DataInput input;
	
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
	public void readData(DataInput input) throws IOException {
		this.input = input;
		super.readData(input);
		type = Type.values()[input.readInt()];
		byte[] bytes = new byte[input.readInt()];
		input.readFully(bytes);
		journalId = new String(bytes);
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		output.writeInt(type.ordinal());
		output.writeInt(journal.getId().length());
		output.write(journal.getId().getBytes());
		journal.writeData(output);
	}

	/**
	 * @return the input
	 */
	public DataInput getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(DataInput input) {
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
