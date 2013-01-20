/**
 * 
 */
package org.restq.cluster.service.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.restq.cluster.Member;
import org.restq.cluster.impl.MemberImpl;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class ClusterInfo implements DataSerializable {
	
	private boolean request;
	
	private Member member = new MemberImpl();
	
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
	public void readData(DataInput input) throws IOException {
		request = input.readBoolean();
		member.readData(input);
		totalMembers = input.readInt();
	}
	
	@Override
	public void writeData(DataOutput output) throws IOException {
		output.writeBoolean(request);
		member.writeData(output);
		output.writeInt(totalMembers);
	}
}
