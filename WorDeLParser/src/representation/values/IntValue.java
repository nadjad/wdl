package representation.values;

import representation.types.BaseType;

public class IntValue extends Value {
	private Integer value;

	public IntValue(Integer value) {
		super();
		this.value = value;
		this.type = BaseType.getInstance("Integer");
	}

	@Override
	public Integer getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		IntValue other = (IntValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
