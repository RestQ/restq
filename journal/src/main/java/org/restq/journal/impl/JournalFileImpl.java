/**
 * 
 */
package org.restq.journal.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicLong;

import org.restq.core.RestQException;
import org.restq.journal.InternalRecord;
import org.restq.journal.JournalFile;
import org.restq.journal.Record;

/**
 * @author ganeshs
 *
 */
public class JournalFileImpl implements JournalFile {
	
	private RandomAccessFile file;
	
	private AtomicLong readPos = new AtomicLong();
	
	private long maxSize;
	
	private boolean full;
	
	public static final short HEADER_SIZE = 8;
	
	public JournalFileImpl() {
	}
	
	public JournalFileImpl(String fileName, long maxSize) {
		if (maxSize <= HEADER_SIZE) {
			throw new RestQException("Max size should be greater than " + HEADER_SIZE);
		}
		try {
			this.file = new RandomAccessFile(fileName, "rw");
			this.maxSize = maxSize;
			if (getSize() == 0) {
				writeHeader();
			} else {
				readHeader();
			}
			readPos.set(file.getFilePointer());
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	/**
	 * @return the full
	 */
	public boolean isFull() {
		return full || getSize() >= maxSize;
	}

	protected void writeHeader() throws IOException {
		// FIXME First 8 bytes are allocated for header. Populating some junk now.
		file.writeLong(0L);
	}
	
	protected void readHeader() throws IOException {
		// FIXME First 8 bytes are allocated for header. Populating some junk now.
		file.readLong();
	}
	
	@Override
	public long getSize() {
		try {
		return file.length();
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}

	@Override
	public InternalRecord readRecord() {
		InternalRecord record = null;
		try {
			file.seek(readPos.get());
			if (file.getFilePointer() < getSize()) {
				record = new InternalRecord();
				record.readData(file);
			}
			readPos.set(file.getFilePointer());
		} catch(IOException e) {
			throw new RestQException(e);
		}
		return record;
	}

	@Override
	public boolean writeRecord(InternalRecord record) {
		try {
			if (getSize() + record.size() > maxSize) {
				full = true;
				return false;
			}
			record.writeData(file);
			file.getFD().sync();
			return true;
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	public void resetReadPosition() {
		readPos.set(0);
	}
	
	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	public static void main(String[] args) {
		JournalFileImpl fileImpl = new JournalFileImpl("test", 1000);
		System.out.println(fileImpl.getSize());
		fileImpl.writeRecord(new InternalRecord((byte)1, new Record(1L, (byte)2, "test".getBytes())));
		fileImpl.writeRecord(new InternalRecord((byte)1, new Record(2L, (byte)2, "test".getBytes())));
		fileImpl.writeRecord(new InternalRecord((byte)1, new Record(3L, (byte)2, "test".getBytes())));
		fileImpl.writeRecord(new InternalRecord((byte)1, new Record(4L, (byte)2, "test".getBytes())));
		fileImpl.close();
		fileImpl = new JournalFileImpl("test", 1000);
		System.out.println(fileImpl.getSize());
		System.out.println(fileImpl.readRecord());
		System.out.println(fileImpl.readRecord());
		System.out.println(fileImpl.readRecord());
		System.out.println(fileImpl.readRecord());
		System.out.println(fileImpl.readRecord());
		System.out.println(fileImpl.readRecord());
		fileImpl.writeRecord(new InternalRecord((byte)1, new Record(5L, (byte)2, "test".getBytes())));
		System.out.println(fileImpl.readRecord());
		fileImpl.close();
	}
}
