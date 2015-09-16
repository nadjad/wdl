package representation.nodes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import representation.Node;
import representation.Operator;
import representation.Port;
import representation.values.Value;

public class Simulation implements Serializable {
	private String userID;
	private String simID;
	private FlowNode flow;
	private Node node;
	private List<Value> inputs;
	private List<String> outputs;
	private Map<String, Value> inMappings;
	private Map<String, String> outMappings;

	public Simulation(String userID, String simID, FlowNode flow,
			List<Value> inputs, List<String> outputs) {
		this(flow, inputs, outputs);
		this.userID = userID;
		this.simID = simID;
	}

	public Simulation(FlowNode flow, List<Value> inputs, List<String> outputs) {
		super();
		this.flow = flow;
		this.inputs = inputs;
		this.outputs = outputs;
		// create the input and output argument mappings
		this.inMappings = new HashMap<String, Value>();
		this.outMappings = new HashMap<String, String>();
		Operator op = this.flow.getOperatorType();
		List<Port> ipts = op.getiList();
		int i = 0;
		for (Port p : ipts) {
			inMappings.put(p.getName(), inputs.get(i));
			i++;
		}
		List<Port> opts = op.getoList();
		i = 0;
		for (Port p : opts) {
			outMappings.put(p.getName(), outputs.get(i));
			i++;
		}
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSimID() {
		return simID;
	}

	public void setSimID(String simID) {
		this.simID = simID;
	}

	public FlowNode getFlowNode() {
		return flow;
	}

	public List<Value> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public Map<String, Value> getInMappings() {
		return inMappings;
	}

	public Map<String, String> getOutMappings() {
		return outMappings;
	}
}
