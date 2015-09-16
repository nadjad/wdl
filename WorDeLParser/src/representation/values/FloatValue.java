package representation.values;

import representation.types.BaseType;

public class FloatValue extends Value {

	private Float value;

	public FloatValue(Float value) {
		super();
		this.value = value;
		this.type = BaseType.getInstance("Float");
	}

	@Override
	public Object getValue() {
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
		FloatValue other = (FloatValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
