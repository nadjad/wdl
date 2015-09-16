package representation.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import representation.Connection;
import representation.Node;
import representation.Operator;

public class FlowNode extends Node {

	private Map<String, Connection> connections;
	private Map<String, Node> nodes;

	/*************************************** CONSTRUCTORS **************************************/

	public FlowNode(String id, Operator operatorType,
			Map<String, Connection> inConnections,
			Map<String, Connection> outConnections,
			Map<String, Connection> connections, Map<String, Node> nodes) {
		super(id, operatorType, inConnections, outConnections);
		this.connections = connections;
		this.nodes = nodes;
	}

	public FlowNode(String id, Operator operatorType,
			Map<String, Connection> connections, Map<String, Node> nodes) {
		super(id, operatorType, null, null);
		this.connections = connections;
		this.nodes = nodes;
	}

	public FlowNode(String id, Operator operatorType,
			Map<String, Connection> connections) {
		super(id, operatorType, null, null);
		this.connections = connections;
		this.nodes = new HashMap<String, Node>();
	}

	public FlowNode(String id, Operator operatorType) {
		super(id, operatorType);
		this.connections = new HashMap<String, Connection>();
		this.nodes = new HashMap<String, Node>();
	}

	public static FlowNode createFlowNode(Operator operator) {
		return new FlowNode("temp_flow", operator, null, null, null, null);
	}

	/*************************************** CONSTRUCTORS **************************************/

	public void addNode(Node node) {
		if (!this.nodes.containsKey(node.getId())) {
			// add the node to the set of the flow
			this.nodes.put(node.getId(), node);
			// add input connections to the global connections set of the flow
			addConnections(node.getInConnections());
			// add output connections to the global connections set of the flow
			addConnections(node.getOutConnections());
		}
	}

	public void addConnection(Connection connection) {
		this.connections.put(connection.getConnectionId(), connection);
	}

	public void addConnections(Map<String, Connection> connections) {
		Set<String> keys = connections.keySet();
		for (String key : keys) {
			this.connections.put(key, connections.get(key));
		}
	}

	public Map<String, Connection> getConnections() {
		return connections;
	}

	public Map<String, Node> getNodes() {
		return nodes;
	}

}
