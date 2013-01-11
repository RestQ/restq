/**
 * 
 */
package org.restq.core.cluster.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author ganeshs
 *
 */
public interface DataSerializable extends Serializable {

	void writeData(DataOutput output) throws IOException;
	
	void readData(DataInput input) throws IOException;
}
