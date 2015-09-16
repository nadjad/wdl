package support;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import representation.CaseItem;
import representation.Connection;
import representation.ConnectionEnd;
import representation.Node;
import representation.Operator;
import representation.Port;
import representation.nodes.FlowNode;
import representation.nodes.Simulation;
import representation.nodes.SwitchNode;
import representation.values.FloatValue;
import representation.values.IntValue;
import representation.values.StringValue;
import representation.values.Value;

/**
 * This class contains the logic used by the parser in order to collect and
 * organize the flow data structure. It contains a collection of Operators,
 * representing the database operators which can be instantiated, together with
 * sets of input and output ports, nodes and connections which make up one flow.
 * These sets are emptied each time a new flow is started.
 * 
 * @author Nandra Cosmin
 *
 */
public class Support {

	private Map<String, String> errorMsg = new HashMap<String, String>();
	// the collection of known (database) operators which can be instantiated
	private java.util.Map<String, Operator> operators = new java.util.HashMap<String, Operator>();
	private java.util.Map<String, FlowNode> flowNodes = new java.util.HashMap<String, FlowNode>();

	// flow input ports
	private java.util.Map<String, Port> inputPorts = new java.util.HashMap<String, Port>();
	// flow output ports
	private java.util.Map<String, Port> outputPorts = new java.util.HashMap<String, Port>();

	// internal flow connections
	private Map<String, Connection> connections = new HashMap<String, Connection>();

	// the collection of internal flow nodes
	private Map<String, Node> nodes = new HashMap<String, Node>();

	public Support() {
		this.errorMsg
				.put("loop",
						"Loop-back attempt detected. Please re-route one end of the connection.\n    ");
		this.errorMsg
				.put("argNr",
						"The size of the provided argument list does not match the declared number of operator arguments \n    ");
		this.errorMsg
				.put("multiSource",
						"Multiple sources detected. The signal already has a source.\n    ");
	}

	public void setOperators(java.util.Map<String, Operator> operators) {
		this.operators = operators;
	}

	public void initialize() {
		connections = new java.util.HashMap<String, Connection>();
		nodes = new HashMap<String, Node>();
		inputPorts = new java.util.HashMap<String, Port>();
		outputPorts = new java.util.HashMap<String, Port>();
		ErrorRepository.reset();
	}

	public Value extractValue(String value, String type) {
		Value val = null;
		switch (type) {
		case "integer":
			val = new IntValue(Integer.valueOf(value));
			break;
		case "float":
			val = new FloatValue(Float.valueOf(value));
			break;
		case "string":
			val = new StringValue(value);
			break;
		default:
			break;
		}
		return val;
	}

	public void addPorts(List<Port> ports, boolean input) {
		for (Port p : ports) {
			if (input) {
				inputPorts.put(p.getName(), p);
				this.connections.put(p.getName(), new Connection(p.getName(),
						new ConnectionEnd(null, p.getName(), p.getType())));
			} else {
				outputPorts.put(p.getName(), p);
				this.connections.put(p.getName(),
						new Connection(p.getName(), null, new ConnectionEnd(
								null, p.getName(), p.getType())));
			}
		}

	}

	private void linkInputNodeConnections(Node node) {
		// deal with input connections
		Collection<Connection> inc = node.getInConnections().values();
		for (Connection nodeConnection : inc) {
			// Connection nodeConnection = node.getInConnection(in);
			Connection globalConnection = this.connections.get(nodeConnection
					.getConnectionId());
			if (globalConnection != null) {
				List<ConnectionEnd> newDestinations = nodeConnection
						.getDestinations();
				for (ConnectionEnd ce : newDestinations) {
					if (!globalConnection.addDestination(ce)) {
						addError(errorMsg.get("loop") + " signal:"
								+ globalConnection.getConnectionId(),
								"semantic");
					}
				}
			} else {
				globalConnection = nodeConnection;
			}
			this.connections.put(globalConnection.getConnectionId(),
					globalConnection);
			node.addInConnection(globalConnection);
		}
	}

	private void linkOutputNodeConnections(Node node) {
		// deal with output connections
		Collection<Connection> ouc = node.getOutConnections().values();
		for (Connection nodeConnection : ouc) {
			// Connection nodeConnection = node.getOutConnection(out);
			Connection globalConnection = this.connections.get(nodeConnection
					.getConnectionId());
			if (globalConnection != null) {
				if (globalConnection.hasOrigin()) {
					// the connection already has a point of origin
					ConnectionEnd origin = globalConnection.getOrigin();
					addError(
							errorMsg.get("multiSource") + " @"
									+ origin.toString() + " signal:"
									+ nodeConnection.getConnectionId(),
							"semantic");
				} else {
					if (globalConnection.hasDestination()) {
						if (!globalConnection.setOrigin(nodeConnection
								.getOrigin()))
							addError(errorMsg.get("loop") + " @ signal:"
									+ globalConnection.getConnectionId(),
									"semantic");
					}
				}
			} else {
				globalConnection = nodeConnection;
			}
			this.connections.put(nodeConnection.getConnectionId(),
					globalConnection);
			node.addOutConnection(globalConnection);
		}
	}

	public Node getOperationNode(String name, String id, List<String> inList,
			List<String> outList) {
		Node node = new Node(id, operators.get(name.trim()));
		if (node.getNumberOfInputPorts() == inList.size()
				&& node.getNumberOfOutputPorts() == outList.size()) {

			// instantiate the node's internal connections
			node.instantiate(inList, outList);
			// link the node's input connections to the flow network
			linkInputNodeConnections(node);
			// link the node's output connections to the flow's network
			linkOutputNodeConnections(node);

			// error and exception handling code that generates messages for
			// the user informing on what went wrong
		} else {
			addError(this.errorMsg.get("argNr") + " @ " + name + ":" + id,
					"semantic");
		}

		// add the node to the flow
		this.nodes.put(id, node);

		return node;
	}

	public SwitchNode createSwitchNode(List<String> in, String condPort,
			List<CaseItem> cases) {

		// create the node and instantiate its internal connections
		SwitchNode node = new SwitchNode(in, condPort, cases);

		// link the node's input connections to the flow network
		linkInputNodeConnections(node);

		// link the node's output connections to the flow's network
		linkOutputNodeConnections(node);

		// link up the condition connection of the switch operator
		Connection condition = node.getConditionConnection();
		// search among the global flow connections to see if we find a match
		Connection found = this.connections.get(condition.getConnectionId());
		if (found != null) {
			found.addDestination(condition.getDestinations().get(0));
		} else {
			found = condition;
		}
		this.connections.put(found.getConnectionId(), found);
		node.setConditionConnection(found);

		// add the node to the flow
		this.nodes.put(node.getId(), node);

		return node;
	}

	public FlowNode buildFlow(String name, List<Port> inports,
			List<Port> outports) {

		Operator flowOp = new Operator(name, "", inports, outports);
		this.operators.put(name, flowOp);
		FlowNode flow = new FlowNode(name, flowOp, connections, nodes);
		addFlow(flow);
		return flow;
	}

	public void addFlow(FlowNode flow) {
		this.flowNodes.put(flow.getId(), flow);
	}

	public Simulation buildSimulation(String simName, String opId,
			String opName, List<Value> inputVals, List<String> outArgs) {
		Operator o = operators.get(opId.trim());
		FlowNode flow = flowNodes.get(opId.trim());
		Simulation s = null;
		System.out.println("*************" + inputVals.size() + "***"
				+ inputVals.get(0).getValue());
		if (o != null && flow != null) {
			s = new Simulation("u1", simName, flow, inputVals, outArgs);
		}
		return s;
	}

	private void addError(String message, String type) {
		ErrorRepository.addError(new Error(message, type));
	}
}
