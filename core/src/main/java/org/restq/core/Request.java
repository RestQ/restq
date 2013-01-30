/**
 * 
 */
package org.restq.core;

import java.io.IOException;


/**
 * @author ganeshs
 *
 */
public abstract class Request implements DataSerializable {

	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		
	}

}
