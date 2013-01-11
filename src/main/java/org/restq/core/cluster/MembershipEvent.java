/**
 * 
 */
package org.restq.core.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.core.cluster.impl.MemberImpl;

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
	public void readData(DataInput input) throws IOException {
		super.readData(input);
		member.readData(input);
		type = Type.values()[input.readInt()];
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		member.writeData(output);
		output.writeInt(type.ordinal());
	}
}
