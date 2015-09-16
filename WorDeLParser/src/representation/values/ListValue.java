package representation.values;

import java.util.List;

import representation.Type;

public class ListValue extends Value {

	private List<Value> list;

	public ListValue() {

	}

	public ListValue(List<Value> values, Type type) {
		this.list = values;
		this.type = type;
	}

	public void add(Value value) {
		this.list.add(value);
	}

	public void remove(Value value) {
		this.list.remove(value);
	}

	@Override
	public List<Value> getValue() {
		// TODO Auto-generated method stub
		return this.list;
	}

}
