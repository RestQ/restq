/**
 * 
 */
package org.restq.journal.impl;

import static org.testng.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.restq.core.RestQException;
import org.restq.journal.InternalRecord;
import org.restq.journal.Record;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author ganeshs
 *
 */
public class JournalFileImplTest {
	
	private static final String TEST_FILE = "test";
	
	@BeforeMethod
	public void setup() {
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionWhenDirectoryIsNotFound() {
		new JournalFileImpl("./test123/test", 1000);
	}
	
	@Test(expectedExceptions=RestQException.class)
	public void shouldThrowExceptionWhenMaxSizeIsLessThanHeaderSize() {
		new JournalFileImpl("test", JournalFileImpl.HEADER_SIZE);
	}
	
	@Test
	public void shouldAllocateHeaderSpaceForNewFiles() {
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 1000);
		assertEquals(file.getSize(), JournalFileImpl.HEADER_SIZE);
	}
	
	@Test
	public void shouldReturnSizeOfFile() throws IOException {
		String content = "1234567890";
		writeContent(TEST_FILE, content);
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 1000);
		assertEquals(file.getSize(), content.getBytes().length);
	}
	
	@Test
	public void shouldReturnFullWhenSizeExceedsMaxSize() throws IOException {
		String content = "1234567890";
		writeContent(TEST_FILE, content);
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 9);
		assertTrue(file.isFull());
	}
	
	@Test
	public void shouldWriteRecord() {
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 1000);
		InternalRecord record = new InternalRecord((byte)1, new Record(1, (byte)1, "test".getBytes()));
		file.writeRecord(record);
		assertEquals(file.getSize(), JournalFileImpl.HEADER_SIZE + record.size());
	}
	
	@Test
	public void shouldReadRecord() {
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 1000);
		InternalRecord record = new InternalRecord((byte)1, new Record(1, (byte)1, "test".getBytes()));
		file.writeRecord(record);
		InternalRecord readRecord = file.readRecord(); 
		assertNotNull(readRecord);
		assertEquals(record.size(), readRecord.size());
	}
	
	@Test
	public void shouldNotWriteRecordWhenSizeIsFull() {
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 30);
		InternalRecord record = new InternalRecord((byte)1, new Record(1, (byte)1, "test".getBytes()));
		file.writeRecord(record);
		assertFalse(file.writeRecord(record));
		assertTrue(file.isFull());
	}
	
	@Test
	public void shouldReturnNullWhenNothingToRead() {
		JournalFileImpl file = new JournalFileImpl(TEST_FILE, 30);
		InternalRecord record = new InternalRecord((byte)1, new Record(1, (byte)1, "test".getBytes()));
		file.writeRecord(record);
		file.readRecord();
		assertNull(file.readRecord());
	}
	
	@AfterMethod
	public void destroy() {
		deleteFile(TEST_FILE);
	}

	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
	private void writeContent(String fileName, String content) throws IOException {
		deleteFile(TEST_FILE);
		File f = new File(TEST_FILE);
		FileOutputStream os = new FileOutputStream(f);
		os.write(content.getBytes());
		os.close();
	}
}
