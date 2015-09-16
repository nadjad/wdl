package representation.types;

import java.util.Arrays;
import java.util.List;

import representation.Type;

public class BaseType implements Type {

	private static String[] TYPES = { "Integer", "String", "Float", "Boolean",
			"File" };
	private static List<String> TYPES_LIST = Arrays.asList(TYPES);
	private String typeName;

	public static BaseType getInstance(String typeName) {
		if (TYPES_LIST.contains(typeName))
			return new BaseType(typeName);
		return null;
	}

	private BaseType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
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
		BaseType other = (BaseType) obj;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.typeName;
	}

}
