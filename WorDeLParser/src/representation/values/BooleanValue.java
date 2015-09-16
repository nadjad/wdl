package representation.values;

import representation.types.BaseType;

public class BooleanValue extends Value {
	private Boolean value;

	public BooleanValue(Boolean value) {
		super();
		this.value = value;
		this.type = BaseType.getInstance("Boolean");
	}

	@Override
	public Object getValue() {
		return value;
	}
}
