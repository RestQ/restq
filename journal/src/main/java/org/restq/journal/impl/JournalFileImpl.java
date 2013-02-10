/**
 * 
 */
package org.restq.journal.impl;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.RestQException;
import org.restq.journal.InternalRecord;
import org.restq.journal.JournalFile;
import org.restq.journal.Record;

/**
 * @author ganeshs
 *
 */
public class JournalFileImpl implements JournalFile {
	
	private FileChannel channel;
	
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
			this.channel = new RandomAccessFile(fileName, "rw").getChannel();
			this.maxSize = maxSize;
			if (getSize() == 0) {
				writeHeader();
			} else {
				readHeader();
			}
			readPos.set(channel.position());
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
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.asLongBuffer().put(0L);
		channel.write(buffer);
	}
	
	protected void readHeader() throws IOException {
		// FIXME First 8 bytes are allocated for header. Populating some junk now.
		ByteBuffer buffer = ByteBuffer.allocate(8);
		channel.read(buffer);
	}
	
	@Override
	public long getSize() {
		try {
		return channel.size();
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}

	@Override
	public InternalRecord readRecord() {
		InternalRecord record = null;
		try {
			channel.position(readPos.get());
			if (channel.position() < getSize()) {
				record = new InternalRecord();
				record.readData(new DataInputWrapper(new DataInputStream(Channels.newInputStream(channel))));
			}
			readPos.set(channel.position());
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
			record.writeData(new DataOutputWrapper(new DataOutputStream(Channels.newOutputStream(channel))));
			channel.force(false);
			return true;
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	public void resetReadPosition() {
		readPos.set(HEADER_SIZE);
	}
	
	public void close() {
		try {
			channel.close();
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		channel.truncate(0);
		writeHeader();
		resetReadPosition();
		long size = input.readLong();
		copyBytes(size, input.getDataInput(), new DataOutputStream(Channels.newOutputStream(channel)));
	}

	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		long size = getSize() - HEADER_SIZE;
		output.writeLong(size);
		channel.position(HEADER_SIZE);
		copyBytes(size, new DataInputStream(Channels.newInputStream(channel)), output.getDataOutput());
	}
	
	private void copyBytes(long size, DataInput input, DataOutput output) throws IOException {
		long bufferCount = size / 1024;
		byte[] bytes = new byte[1024];
		for (int i = 0; i < bufferCount; i++) {
			input.readFully(bytes);
			output.write(bytes);
		}
		if (size % 1024 > 0) {
			bytes = new byte[(int)(size % 1024)];
			input.readFully(bytes);
			output.write(bytes);
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
