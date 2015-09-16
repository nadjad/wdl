package representation.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.CaseItem;
import representation.Connection;
import representation.ConnectionEnd;
import representation.Node;
import representation.values.Value;

public class SwitchNode extends Node {

	private static int globalId;
	private Connection condition;
	private Map<String, Connection> inCons;
	private Map<Value, List<Connection>> switchConnections;
	private List<CaseItem> swCases;
	private List<String> inList;

	public SwitchNode(List<String> inList, String condPort, List<CaseItem> cases) {
		super(null, null);
		this.id = "switch_" + globalId;
		globalId++;
		this.swCases = cases;
		this.inList = inList;
		this.switchConnections = new HashMap<Value, List<Connection>>();

		// add input connections
		for (String in : inList) {
			Connection c = this.inConnections.get(in);
			if (c == null) {
				c = new Connection(in);
			}
			c.addDestination(new ConnectionEnd(this.id, in, null));
			this.inConnections.put(in, c);
		}

		// create the condition connection
		condition = new Connection(condPort, null, new ConnectionEnd(this.id,
				condPort, null));
		this.inCons = new HashMap<String, Connection>(this.inConnections);
		this.inCons.put(condition.getConnectionId(), condition);

		// add output switch connections
		for (CaseItem ci : cases) {
			Value val = ci.getValue();
			List<String> outList = ci.getPorts();
			List<Connection> conns = new ArrayList<Connection>();
			for (String out : outList) {
				Connection con = new Connection(out, new ConnectionEnd(this.id,
						out, null));
				conns.add(con);
				this.outConnections.put(con.getConnectionId(), con);
			}
			this.switchConnections.put(val, conns);
		}
	}

	@Override
	public void instantiate(List<String> inList, List<String> outList) {
	}

	public void setConditionConnection(Connection condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		String s = this.id + "\n\t input connections:\n";
		Collection<Connection> vals = this.inConnections.values();
		for (Connection c : vals)
			s = s + c.toString() + "\n";
		s = s + " \n******** condition connection:\n"
				+ this.condition.toString();
		s = s + "output connections:\n\n";
		Set<Value> keys = this.switchConnections.keySet();
		for (Value key : keys) {
			List<Connection> ocons = this.switchConnections.get(key);
			s = s + "----value:" + key.getValue().toString() + "\n";
			for (Connection c : ocons) {
				s = s + c.toString();
			}
		}
		return s;
	}

	public Connection getConditionConnection() {
		return this.condition;
	}

	@Override
	public int getNumberOfInputPorts() {
		// TODO Auto-generated method stub
		return this.inCons.size();
	}

	@Override
	public int getNumberOfOutputPorts() {
		// TODO Auto-generated method stub
		return this.outConnections.size();
	}

	public Map<Value, List<Connection>> getSwitchConnections() {
		return switchConnections;
	}

	public List<CaseItem> getSwCases() {
		return swCases;
	}

	public List<String> getInList() {
		return inList;
	}

}
