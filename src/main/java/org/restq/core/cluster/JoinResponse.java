/**
 * 
 */
package org.restq.core.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.restq.core.cluster.impl.MemberImpl;

/**
 * @author ganeshs
 *
 */
public class JoinResponse extends Response {

	private Set<Member> members = new HashSet<Member>();
	
	private Member master;
	
	public JoinResponse() {
	}
	
	/**
	 * @param members
	 */
	public JoinResponse(Set<Member> members, Member master) {
		this.members = members;
		this.master = master;
	}

	/**
	 * @return the members
	 */
	public Set<Member> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	/**
	 * @return the master
	 */
	public Member getMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(Member master) {
		this.master = master;
	}

	@Override
	public void writeData(DataOutput output) throws IOException {
		super.writeData(output);
		output.writeInt(members.size());
		for (Member member : members) {
			member.writeData(output);
		}
		master.writeData(output);
	}

	@Override
	public void readData(DataInput input) throws IOException {
		super.readData(input);
		int memberSize = input.readInt();
		for (int i=0; i < memberSize; i++) {
			Member member = new MemberImpl();
			member.readData(input);
			members.add(member);
		}
		master = new MemberImpl();
		master.readData(input);
	}

	@Override
	public String toString() {
		return "JoinResponse [members=" + members + ", master=" + master + "]";
	}
}
