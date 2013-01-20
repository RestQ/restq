/**
 * 
 */
package org.restq.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public abstract class Event implements DataSerializable {

	/* (non-Javadoc)
	 * @see org.restq.cluster.nio.DataSerializable#writeData(java.io.DataOutput)
	 */
	@Override
	public void writeData(DataOutput output) throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.restq.cluster.nio.DataSerializable#readData(java.io.DataInput)
	 */
	@Override
	public void readData(DataInput input) throws IOException {
		// TODO Auto-generated method stub

	}

}
