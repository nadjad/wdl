package representation.nodes;

import java.io.Serializable;
import java.util.Map;

public class ContentNode implements Serializable {

	private Map<String, FlowNode> flows;
	private Simulation simulation;

	public ContentNode() {
		super();
	}

	public ContentNode(Map<String, FlowNode> flows) {
		super();
		this.flows = flows;
	}

	public ContentNode(Map<String, FlowNode> flows, Simulation simulation) {
		super();
		this.flows = flows;
		this.simulation = simulation;
	}

	public Map<String, FlowNode> getFlows() {
		return flows;
	}

	public FlowNode getFlow(String name) {
		if (this.flows != null)
			return this.flows.get(name);
		return null;
	}

	public void setFlows(Map<String, FlowNode> flows) {
		this.flows = flows;
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

}
