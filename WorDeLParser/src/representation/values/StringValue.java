package representation.values;

import representation.types.BaseType;

public class StringValue extends Value {
	private String value;

	public StringValue(String value) {
		super();
		this.value = value;
		this.type = BaseType.getInstance("String");
	}

	@Override
	public Object getValue() {
		return this.value;
	}

}
