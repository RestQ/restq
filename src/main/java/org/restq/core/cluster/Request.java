/**
 * 
 */
package org.restq.core.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.cluster.nio.DataSerializable;


/**
 * @author ganeshs
 *
 */
public abstract class Request implements DataSerializable {

	@Override
	public void writeData(DataOutput output) throws IOException {
		
	}

	@Override
	public void readData(DataInput input) throws IOException {
		
	}

}
