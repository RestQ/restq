/**
 * 
 */
package org.restq.cluster;

import java.io.IOException;

import org.restq.cluster.impl.MemberImpl;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.Request;

/**
 * @author ganeshs
 *
 */
public class UnjoinRequest extends Request {

	private Member member;
	
	public UnjoinRequest() {
		member = new MemberImpl();
	}
	
	/**
	 * @param member
	 */
	public UnjoinRequest(Member member) {
		this.member = member;
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		member.writeData(output);
	}
	
	@Override
	public void readData(DataInputWrapper input)  throws IOException {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnjoinRequest other = (UnjoinRequest) obj;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		return true;
	}
}
