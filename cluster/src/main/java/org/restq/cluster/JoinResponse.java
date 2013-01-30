/**
 * 
 */
package org.restq.cluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.restq.cluster.impl.MemberImpl;
import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.Response;

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
	public void writeData(DataOutputWrapper output) throws IOException {
		super.writeData(output);
		output.writeInt(members.size());
		for (Member member : members) {
			member.writeData(output);
		}
		output.writeBoolean(master != null);
		if (master != null) {
			master.writeData(output);
		}
	}

	@Override
	public void readData(DataInputWrapper input) throws IOException {
		super.readData(input);
		int memberSize = input.readInt();
		for (int i=0; i < memberSize; i++) {
			Member member = new MemberImpl();
			member.readData(input);
			members.add(member);
		}
		if (input.readBoolean()) {
			master = new MemberImpl();
			master.readData(input);
		}
	}

	@Override
	public String toString() {
		return "JoinResponse [members=" + members + ", master=" + master + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((master == null) ? 0 : master.hashCode());
		result = prime * result + ((members == null) ? 0 : members.hashCode());
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
		JoinResponse other = (JoinResponse) obj;
		if (master == null) {
			if (other.master != null)
				return false;
		} else if (!master.equals(other.master))
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		return true;
	}
}
