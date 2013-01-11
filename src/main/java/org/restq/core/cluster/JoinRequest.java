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
public class JoinRequest extends Request {

	private Member member;
	
	public JoinRequest() {
		member = new MemberImpl();
	}
	
	/**
	 * @param member
	 */
	public JoinRequest(Member member) {
		this.member = member;
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		member.writeData(output);
	}
	
	@Override
	public void readData(DataInput input)  throws IOException {
		super.readData(input);
		member.readData(input);
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "JoinRequest [member=" + member + "]";
	}
}
