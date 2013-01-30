/**
 * 
 */
package org.restq.cluster;

import java.io.IOException;

import org.restq.cluster.impl.MemberImpl;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;

/**
 * @author ganeshs
 *
 */
public class MembershipEvent extends Event {
	
	private Member member;
	
	private Type type;
	
	/**
	 * @author ganeshs
	 *
	 */
	public enum Type {
		added, removed
	}

	/**
	 * 
	 */
	public MembershipEvent() {
		member = new MemberImpl();
	}
	
	/**
	 * @param member
	 * @param type
	 */
	public MembershipEvent(Member member, Type type) {
		this.member = member;
		this.type = type;
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
		member.readData(input);
		type = Type.values()[input.readInt()];
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		member.writeData(output);
		output.writeInt(type.ordinal());
	}
}
