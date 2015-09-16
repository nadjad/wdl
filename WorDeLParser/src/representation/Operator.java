package representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operator implements Serializable {

	protected String name;
	protected String path;
	protected Map<String, Port> inputs;
	protected Map<String, Port> outputs;
	protected List<Port> iList;
	protected List<Port> oList;

	public Operator(String name, String path) {
		super();
		this.name = name;
		this.path = path;
		this.inputs = new HashMap<String, Port>();
		this.outputs = new HashMap<String, Port>();
		this.iList = new ArrayList<Port>();
		this.oList = new ArrayList<Port>();
	}

	public Operator(String name, String path, List<Port> inputs,
			List<Port> outputs) {
		this(name, path);
		for (Port p : inputs)
			this.inputs.put(p.getName(), p);
		for (Port p : outputs)
			this.outputs.put(p.getName(), p);
		this.iList = inputs;
		this.oList = outputs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Port> getInputs() {
		return inputs;
	}

	public void setInputs(Map<String, Port> inputs) {
		this.inputs = inputs;
	}

	public Map<String, Port> getOutputs() {
		return outputs;
	}

	public Port getInputPort(int nr) {
		return this.inputs.get(nr);
	}

	public Port getOutputPort(int nr) {
		return this.outputs.get(nr);
	}

	public void setOutputs(Map<String, Port> outputs) {
		this.outputs = outputs;
	}

	public List<Port> getiList() {
		return iList;
	}

	public List<Port> getoList() {
		return oList;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", inputs=" + inputs + ", outputs="
				+ outputs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Operator other = (Operator) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
