package representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
	protected Operator operatorType;
	protected Map<String, Port> inPorts;
	protected Map<String, Port> outPorts;
	protected List<Port> iList;
	protected List<Port> oList;
	protected Map<String, Connection> inConnections;
	protected Map<String, Connection> outConnections;

	public Node(String id, Operator operatorType,
			Map<String, Connection> inConnections,
			Map<String, Connection> outConnections) {
		this(id, operatorType);
		if (inConnections != null)
			this.inConnections = inConnections;
		if (outConnections != null)
			this.outConnections = outConnections;
	}

	public Node(String id, Operator operatorType) {
		super();
		this.id = id;
		if (operatorType != null) {
			this.operatorType = operatorType;
			this.inPorts = this.operatorType.getInputs();
			this.outPorts = this.operatorType.getOutputs();
			this.iList = this.operatorType.getiList();
			this.oList = this.operatorType.getoList();
		} else {
			System.out.println("e null");
			this.inPorts = new HashMap<String, Port>();
			this.outPorts = new HashMap<String, Port>();
			this.iList = new ArrayList<Port>();
			this.oList = new ArrayList<Port>();
		}
		this.inConnections = new HashMap<String, Connection>();
		this.outConnections = new HashMap<String, Connection>();
	}

	public String getId() {
		return id;
	}

	public Operator getOperatorType() {
		return operatorType;
	}

	public Map<String, Port> getInPorts() {
		return inPorts;
	}

	public Map<String, Port> getOutPorts() {
		return outPorts;
	}

	public Map<String, Connection> getInConnections() {
		return inConnections;
	}

	public Map<String, Connection> getOutConnections() {
		return outConnections;
	}

	// public void addInConnections(Map<String, Connection> inConnections) {
	// Set<String> keys = inConnections.keySet();
	// for (String key : keys)
	// this.inConnections.put(key, inConnections.get(key));
	// }

	public void addOutConnections(Map<String, Connection> outConnections) {
		Set<String> keys = outConnections.keySet();
		for (String key : keys)
			this.outConnections.put(key, outConnections.get(key));
	}

	public void addInConnection(Connection connection) {
		List<ConnectionEnd> destinations = connection.getDestinations();
		for (ConnectionEnd ce : destinations) {
			if (ce.getNodeId().equals(this.id)) {
				this.inConnections.put(ce.getNodePortId(), connection);
			}
		}
		// this.inConnections.put(connection.getConnectionId(), connection);
	}

	public void addOutConnection(Connection connection) {
		ConnectionEnd origin = connection.getOrigin();
		this.outConnections.put(origin.getNodePortId(), connection);
		// this.outConnections.put(connection.getConnectionId(), connection);
	}

	public Connection getConnection(String connectionName) {
		Connection connection = this.inConnections.get(connectionName);
		if (connection == null)
			connection = this.outConnections.get(connectionName);
		return connection;
	}

	// public Connection getConnectionByPort(String connectionName) {
	// Connection connection = this.inConnections.get(connectionName);
	// if (connection == null)
	// connection = this.outConnections.get(connectionName);
	// return connection;
	// }
	public Connection getInConnection(String identifier) {
		return this.inConnections.get(identifier);
	}

	public Connection getOutConnection(String identifier) {
		return this.outConnections.get(identifier);
	}

	public int getNumberOfInputPorts() {
		return this.inPorts.size();
	}

	public int getNumberOfOutputPorts() {
		return this.outPorts.size();
	}

	public void instantiate(List<String> inList, List<String> outList) {
		int i = 0;
		for (String in : inList) {
			Connection conn = this.inConnections.get(in);
			if (conn == null) {
				this.inConnections
						.put(this.iList.get(i).getName(), new Connection(in,
								null, createConnEndforPort(i, true)));
			} else {
				conn.addDestination(createConnEndforPort(i, true));
			}
			i++;
		}

		i = 0;
		for (String out : outList) {
			this.outConnections.put(this.oList.get(i).getName(),
					new Connection(out, createConnEndforPort(i, false)));
			i++;
		}
	}

	private ConnectionEnd createConnEndforPort(int number, boolean in) {
		ConnectionEnd connEnd;
		if (in) {
			connEnd = new ConnectionEnd(this.id, this.iList.get(number)
					.getName(), this.iList.get(number).getType());
		} else {
			connEnd = new ConnectionEnd(this.id, this.oList.get(number)
					.getName(), this.iList.get(number).getType());
		}
		return connEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((operatorType == null) ? 0 : operatorType.hashCode());
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
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operatorType == null) {
			if (other.operatorType != null)
				return false;
		} else if (!operatorType.equals(other.operatorType))
			return false;
		return true;
	}

}
