/**
 * 
 */
package org.restq.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


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
