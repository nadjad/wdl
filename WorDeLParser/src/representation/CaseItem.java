package representation;

import java.util.List;

import representation.values.Value;

public class CaseItem {
	private Value value;
	private List<String> portNames;

	public CaseItem(Value value, List<String> ports) {
		super();
		this.value = value;
		this.portNames = ports;
	}

	public Value getValue() {
		return value;
	}

	public List<String> getPorts() {
		return portNames;
	}

}
