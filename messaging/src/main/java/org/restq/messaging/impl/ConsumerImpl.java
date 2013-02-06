/**
 * 
 */
package org.restq.messaging.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.messaging.Consumer;
import org.restq.messaging.Filter;

/**
 * @author ganeshs
 *
 */
public class ConsumerImpl implements Consumer {
	
	private String id;
	
	private String destination;
	
	private List<Filter> filters = new ArrayList<Filter>();
	
	public ConsumerImpl() {
	}
	
	public ConsumerImpl(String id, String destination) {
		this.id = id;
		this.destination = destination;
	}

	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	@Override
	public List<Filter> getFilters() {
		return filters;
	}

	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		id = input.readString();
		destination = input.readString();
		filters = input.readList(new DataInputWrapper.DataSerializableBuilder<Filter>() {
			@Override
			public Filter newInstance() {
				return new Filter();
			}
		});
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(id);
		output.writeString(destination);
		output.writeList(filters);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsumerImpl other = (ConsumerImpl) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConsumerImpl [id=" + id + ", destination=" + destination + "]";
	}
}
