/**
 * 
 */
package org.restq.core;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author ganeshs
 *
 */
public interface DataSerializable extends Serializable {

	void writeData(DataOutputWrapper output) throws IOException;
	
	void readData(DataInputWrapper input) throws IOException;
}
