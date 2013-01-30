/**
 * 
 */
package org.restq.cluster.service.impl;

import java.io.IOException;

import org.restq.cluster.Member;
import org.restq.cluster.impl.MemberImpl;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class ClusterInfo implements DataSerializable {
	
	private boolean request;
	
	private Member member;
	
	private int totalMembers;
	
	public ClusterInfo() {
	}

	/**
	 * @param request
	 * @param member
	 * @param totalMembers
	 */
	public ClusterInfo(boolean request, Member member, int totalMembers) {
		this.request = request;
		this.member = member;
		this.totalMembers = totalMembers;
	}

	/**
	 * @return the request
	 */
	public boolean isRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(boolean request) {
		this.request = request;
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

	/**
	 * @return the totalMembers
	 */
	public int getTotalMembers() {
		return totalMembers;
	}

	/**
	 * @param totalMembers the totalMembers to set
	 */
	public void setTotalMembers(int totalMembers) {
		this.totalMembers = totalMembers;
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		request = input.readBoolean();
		member =  new MemberImpl();
		member.readData(input);
		totalMembers = input.readInt();
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeBoolean(request);
		member.writeData(output);
		output.writeInt(totalMembers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		result = prime * result + (request ? 1231 : 1237);
		result = prime * result + totalMembers;
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
		ClusterInfo other = (ClusterInfo) obj;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		if (request != other.request)
			return false;
		if (totalMembers != other.totalMembers)
			return false;
		return true;
	}
}
