package representation.values;

import java.io.Serializable;

import representation.Type;

public abstract class Value implements Serializable {
	protected Type type;

	public Type getType() {
		return this.type;
	}

	public abstract Object getValue();

	// @Override
	// public abstract String toString();
}
