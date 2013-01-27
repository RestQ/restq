/**
 * 
 */
package org.restq.journal.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restq.core.RestQException;
import org.restq.journal.InternalRecord;
import org.restq.journal.Journal;
import org.restq.journal.JournalFile;
import org.restq.journal.JournalListener;
import org.restq.journal.JournalReaderCallback;
import org.restq.journal.Record;

/**
 * @author ganeshs
 *
 */
public class JournalImpl implements Journal {
	
	private String id;
	
	private String filePrefix;
	
	private String fileSuffix;
	
	private String fileDir;
	
	private int minFiles;
	
	private long maxFileSize;
	
	private List<JournalFile> files = new ArrayList<JournalFile>();
	
	private JournalFile currentFile = null;
	
	private List<JournalListener> listeners = new ArrayList<JournalListener>();
	
	public static final int ADD_RECORD_MARKER = 1;
	
	public static final int UPDATE_RECORD_MARKER = 2;
	
	public static final int DELETE_RECORD_MARKER = 3;
	
	/**
	 * @param id
	 * @param fileDir
	 * @param filePrefix
	 * @param fileSuffix
	 * @param minFiles
	 */
	public JournalImpl(String id, String fileDir, String filePrefix, String fileSuffix, int minFiles, long maxFileSize) {
		this.id = id;
		this.fileDir = fileDir;
		this.filePrefix = filePrefix;
		this.fileSuffix = fileSuffix;
		this.minFiles = minFiles;
		this.maxFileSize = maxFileSize;
		load();
	}
	
	protected void load() {
		File file = new File(fileDir);
		if (!file.exists() || !file.isDirectory()) {
			throw new RestQException("Folder " + fileDir + " doesn't exist");
		}
		String[] fileNames = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + fileSuffix);
			}
		});
		
		for (String fileName : fileNames) {
			addJournalFile(fileDir + "/" + fileName);
		}

		// Create new files to ensure minimum number of files are pre-created
		for (int i = fileNames.length; i < minFiles; i++) {
			addNewJournalFile();
		}
	}
	
	protected JournalFile addNewJournalFile() {
		String fileName = fileDir + "/" + filePrefix + "_" + (files.size() + 1) + "." + fileSuffix;
		return addJournalFile(fileName);
	}
	
	protected JournalFile addJournalFile(String fileName) {
		JournalFile file = loadJournalFile(fileName);
		files.add(file);
		return file;
	}
	
	protected JournalFile loadJournalFile(String fileName) {
		JournalFileImpl file = new JournalFileImpl(fileName, maxFileSize);
		return file;
	}
	
	public void readRecords(JournalReaderCallback callback) {
		InternalRecord record = null;
		for (JournalFile file : files) {
			((JournalFileImpl)file).resetReadPosition();
			while((record = file.readRecord()) != null) {
				switch(record.getMarker()) {
				case ADD_RECORD_MARKER:
					callback.readAddRecord(record.getRecord());
					break;
				case UPDATE_RECORD_MARKER:
					callback.readUpdateRecord(record.getRecord());
					break;
				case DELETE_RECORD_MARKER:
					callback.readDeleteRecord(record.getRecord());
					break;
				}
			}
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void appendAddRecord(Record record) {
		appendRecord(new InternalRecord(ADD_RECORD_MARKER, record));
	}

	@Override
	public void appendUpdateRecord(Record record) {
		appendRecord(new InternalRecord(UPDATE_RECORD_MARKER, record));
	}

	@Override
	public void appendDeleteRecord(Record record) {
		appendRecord(new InternalRecord(DELETE_RECORD_MARKER, record));
	}
	
	protected void appendRecord(InternalRecord record) {
		boolean success = getFileToUse(false).writeRecord(record);
		while (! success) {
			success = getFileToUse(true).writeRecord(record);
		}
	}

	/**
	 * @return the filePrefix
	 */
	public String getFilePrefix() {
		return filePrefix;
	}

	/**
	 * @param filePrefix the filePrefix to set
	 */
	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}

	/**
	 * @param fileSuffix the fileSuffix to set
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	/**
	 * @return the minFiles
	 */
	public int getMinFiles() {
		return minFiles;
	}

	/**
	 * @param minFiles the minFiles to set
	 */
	public void setMinFiles(int minFiles) {
		this.minFiles = minFiles;
	}

	/**
	 * @return the fileDir
	 */
	public String getFileDir() {
		return fileDir;
	}

	/**
	 * @param fileDir the fileDir to set
	 */
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
	
	@Override
	public List<JournalFile> getFiles() {
		return files;
	}
	
	protected JournalFile getFileToUse(boolean force) {
		if (force || currentFile == null) {
			for (JournalFile file : files) {
				if (! file.isFull()) {
					currentFile = file;
					break;
				}
				currentFile = null;
			}
		}
		if (currentFile == null) {
			currentFile = addNewJournalFile();
		}
		
		return currentFile;
	}
	
	@Override
	public void close() {
		for (JournalFile file : files) {
			file.close();
		}
		files.clear();
	}
	
	@Override
	public void readData(DataInput input) throws IOException {
		int size = input.readInt();
		for (int i = 0; i < size; i++) {
			if (files.size() <= i) {
				addNewJournalFile();
			}
			files.get(i).readData(input);
		}
		
		if (size > 0) {
			for (JournalListener listener : listeners) {
				listener.journalUpdated(this);
			}
		}
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeInt(files.size());
		for (JournalFile file : files) {
			file.writeData(output);
		}
	}
	
	@Override
	public void addListener(JournalListener listener) {
		this.listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JournalImpl other = (JournalImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
