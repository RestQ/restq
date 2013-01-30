/**
 * 
 */
package org.restq.journal.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.restq.journal.InternalRecord;
import org.restq.journal.JournalReaderCallback;
import org.restq.journal.Record;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author ganeshs
 *
 */
public class JournalImplTest {
	
	private static final String TEST_DIR = "test";
	
	@BeforeClass
	public void beforeClass() {
		File file = new File(TEST_DIR);
		file.mkdir();
	}

	@BeforeMethod
	public void setup() {
		teardown();
	}
	
	@Test
	public void shouldLoadJournalFiles() throws IOException {
		createFiles(TEST_DIR, "test", "txt", 1, 10);
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 1000);
		assertEquals(journal.getFiles().size(), 10);
	}
	
	@Test
	public void shouldCreateNewFilesWhenMinFilesAreNotThere() throws IOException {
		createFiles(TEST_DIR, "test", "txt", 1, 3);
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 1000);
		assertEquals(journal.getFiles().size(), 5);
	}
	
	@Test
	public void shouldCreateMinFilesWhenNoFilesArePresent() throws IOException {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 1000);
		assertEquals(journal.getFiles().size(), 5);
	}
	
	@Test
	public void shouldLoadFilesOnlyWithSpecifiedExtension() throws IOException {
		createFiles(TEST_DIR, "test", "txt", 1, 7);
		createFiles(TEST_DIR, "test", "log", 1, 3);
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 1000);
		assertEquals(journal.getFiles().size(), 7);
	}
	
	@Test
	public void shouldAppendAddRecord() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendAddRecord(record);
		assertTrue(journal.getFiles().get(0).getSize() > 10);
		InternalRecord ir = journal.getFiles().get(0).readRecord();
		assertNotNull(ir);
		assertEquals(ir.getMarker(), JournalImpl.ADD_RECORD_MARKER);
	}
	
	@Test
	public void shouldAppendUpdateRecord() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendUpdateRecord(record);
		assertTrue(journal.getFiles().get(0).getSize() > 10);
		InternalRecord ir = journal.getFiles().get(0).readRecord();
		assertNotNull(ir);
		assertEquals(ir.getMarker(), JournalImpl.UPDATE_RECORD_MARKER);
	}
	
	@Test
	public void shouldAppendDeleteRecord() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendDeleteRecord(record);
		assertTrue(journal.getFiles().get(0).getSize() > 10);
		InternalRecord ir = journal.getFiles().get(0).readRecord();
		assertNotNull(ir);
		assertEquals(ir.getMarker(), JournalImpl.DELETE_RECORD_MARKER);
	}
	
	@Test
	public void shouldAppendAddRecordToSubsquentFiles() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 5, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendAddRecord(record);
		journal.appendAddRecord(record);
		assertTrue(journal.getFiles().get(0).getSize() > 10);
		assertTrue(journal.getFiles().get(1).getSize() > 10);
	}
	
	@Test
	public void shouldCreateNewFileWhenAppendAddRecordFails() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 1, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendAddRecord(record);
		journal.appendAddRecord(record);
		assertTrue(journal.getFiles().get(0).getSize() > 10);
		assertTrue(journal.getFiles().get(1).getSize() > 10);
	}
	
	@Test
	public void shouldReceiveCallBackOnReadRecords() {
		JournalImpl journal = new JournalImpl("id1", TEST_DIR, "test", "txt", 1, 30);
		Record record = new Record(1, (byte)1, "test".getBytes());
		journal.appendAddRecord(record);
		journal.appendUpdateRecord(record);
		journal.appendDeleteRecord(record);
		JournalReaderCallback callback = mock(JournalReaderCallback.class);
		journal.readRecords(callback);
		verify(callback).readAddRecord(record);
		verify(callback).readUpdateRecord(record);
		verify(callback).readDeleteRecord(record);
	}
	
	@AfterMethod
	public void teardown() {
		File file = new File(TEST_DIR);
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
		});
		for (File f : files) {
			f.delete();
		}
	}
	
	@AfterClass
	public void afterClass() {
		File file = new File(TEST_DIR);
		file.delete();
	}
	
	private void createFiles(String dir, String prefix, String extension, int startIndex, int endIndex) throws IOException {
		for (int i = startIndex; i <= endIndex; i++) {
			File file = new File(dir + "/" + prefix + "_" + i + "." + extension);
			file.createNewFile();
		}
	}
}
