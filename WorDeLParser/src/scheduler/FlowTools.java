package scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Connection;
import representation.Node;
import representation.Port;
import representation.nodes.FlowNode;

public class FlowTools {

	public static Set<Set<Node>> topologicalSort(FlowNode flow) {
		Set<Set<Node>> levels = new HashSet<Set<Node>>();
		List<String> inPorts = getPortNames(flow);
		Collection<Node> nodes = flow.getNodes().values();
		// get level 1 nodes
		Set<Node> level = new HashSet<Node>();
		for (Node node : nodes) {
			Collection<Connection> cons = node.getInConnections().values();
			for (Connection con : cons) {
				// List<ConnectionEnd> destinations = con.getDestinations();
				// for(ConnectionEnd ce:destinations){
				//
				// }
				// if (con.connectsTo(nodeId, portId)) {

				// }
			}
		}
		return levels;
	}

	private static List<String> getPortNames(FlowNode flow) {
		Map<String, Port> ports = flow.getInPorts();
		List<String> pNames = new ArrayList<String>();
		for (Port p : ports.values()) {
			pNames.add(p.getName());
		}
		return pNames;
	}
}
