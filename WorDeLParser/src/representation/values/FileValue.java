package representation.values;

import representation.types.BaseType;

public class FileValue extends Value {
	private String value;

	public FileValue(String value) {
		this.value = value;
		this.type = BaseType.getInstance("File");
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}

}
