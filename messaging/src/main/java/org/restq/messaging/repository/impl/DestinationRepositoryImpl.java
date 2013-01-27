/**
 * 
 */
package org.restq.messaging.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restq.core.RestQException;
import org.restq.core.Serializer;
import org.restq.journal.Journal;
import org.restq.journal.JournalListener;
import org.restq.journal.JournalReaderCallback;
import org.restq.journal.Record;
import org.restq.journal.repository.JournalRepository;
import org.restq.messaging.Destination;
import org.restq.messaging.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ganeshs
 *
 */
public class DestinationRepositoryImpl implements DestinationRepository, JournalListener {
	
	private Map<String, Destination> destinations = new HashMap<String, Destination>();

	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private Serializer serializer;
	
	public void init() {
		Journal journal = journalRepository.getDestinationsJournal();
		journal.addListener(this);
		refresh();
	}
	
	public void refresh() {
		Journal journal = journalRepository.getDestinationsJournal();
		journal.readRecords(new JournalReaderCallback() {
			
			@Override
			public void readUpdateRecord(Record record) {
				Destination destination = getDestination(record);
				if (destination != null) {
					destinations.put(destination.getName(), destination);
				}
			}
			
			@Override
			public void readDeleteRecord(Record record) {
				Destination destination = getDestination(record);
				if (destination != null) {
					destinations.remove(destination.getName());
				}
			}
			
			@Override
			public void readAddRecord(Record record) {
				Destination destination = getDestination(record);
				if (destination != null) {
					destinations.put(destination.getName(), destination);
				}
			}
		});
	}
	
	protected Destination getDestination(Record record) {
		Destination destination = null;
		try {
			destination = (Destination) serializer.deserialize(record.getData());
		} catch (IOException e) {
			// TODO handle exception
		}
		return destination;
	}
	
	@Override
	public Destination find(String id) {
		return destinations.get(id);
	}
	
	@Override
	public void create(Destination destination) {
		save(destination, false);
	}
	
	@Override
	public void delete(Destination destination) {
		destinations.remove(destination.getName());
		Record record = getRecord(destination);
		journalRepository.getDestinationsJournal().appendDeleteRecord(record);
	}
	
	protected void save(Destination destination, boolean update) {
		destinations.put(destination.getName(), destination);
		Record record = getRecord(destination);
		if (! update) {
			journalRepository.getDestinationsJournal().appendAddRecord(record);
		} else {
			journalRepository.getDestinationsJournal().appendUpdateRecord(record);
		}
	}
	
	protected Record getRecord(Destination destination) {
		try {
			return new Record(1, JournalRepository.DESTINATION, serializer.serialize(destination));
		} catch (IOException e) {
			throw new RestQException(e);
		}
	}
	
	@Override
	public void save(Destination destination) {
		save(destination, true);
	}

	@Override
	public List<Destination> findAll() {
		return new ArrayList<Destination>(destinations.values());
	}
	
	@Override
	public void journalUpdated(Journal journal) {
		refresh();
	}
}
