/**
 * 
 */
package org.restq.cluster;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public abstract class Event implements DataSerializable {

	@Override
	public void writeData(DataOutputWrapper output) throws IOException {

	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {

	}

}
