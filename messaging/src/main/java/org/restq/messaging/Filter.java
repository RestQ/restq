/**
 * 
 */
package org.restq.messaging;

import java.io.IOException;

import org.restq.core.DataInputWrapper;
import org.restq.core.DataOutputWrapper;
import org.restq.core.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class Filter implements DataSerializable {
	
	public enum Operator {
		eq {
			@Override
			public boolean matches(String value1, String value2) {
				if (value1 == null || value2 == null) {
					return false;
				}
				return value1.equals(value2);
			}
		},
		
		contains {
			@Override
			public boolean matches(String value1, String value2) {
				if (value1 == null || value2 == null) {
					return false;
				}
				return value1.contains(value2);
			}
		};
		
		public abstract boolean matches(String value1, String value2);
	}

	private String name;
	
	private String value;
	
	private Operator operator = Operator.eq;
	
	public Filter() {
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public Filter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @param name
	 * @param value
	 * @param operator
	 */
	public Filter(String name, String value, Operator operator) {
		this(name, value);
		this.operator = operator;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public boolean matches(String value) {
		return operator.matches(value, this.value);
	}
	
	@Override
	public void readData(DataInputWrapper input) throws IOException {
		name = input.readString();
		value = input.readString();
		operator = Operator.values()[input.readInt()];
	}
	
	@Override
	public void writeData(DataOutputWrapper output) throws IOException {
		output.writeString(name);
		output.writeString(value);
		output.writeInt(operator.ordinal());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Filter other = (Filter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (operator != other.operator)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Filter [name=" + name + ", value=" + value + ", operator="
				+ operator + "]";
	}
	
}
