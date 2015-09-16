package representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Serializable {

	private String connectionId;
	private ConnectionEnd origin;
	private Type type;

	private List<ConnectionEnd> destinations;

	/*********************************** CONSTRUCTORS ****************************************/

	public Connection(String connectionId) {
		super();
		this.connectionId = connectionId;
		this.destinations = new ArrayList<ConnectionEnd>();
	}

	public Connection(String connectionId, ConnectionEnd origin) {
		this(connectionId);
		this.origin = origin;
	}

	public Connection(String connectionId, ConnectionEnd origin,
			ConnectionEnd destination) {
		this(connectionId, origin);
		this.destinations.add(destination);
	}

	public Connection(String connectionId, ConnectionEnd origin,
			List<ConnectionEnd> destinations) {
		this(connectionId, origin);
		this.destinations = destinations;
	}

	/*********************************** CONSTRUCTORS ****************************************/

	public ConnectionEnd getOrigin() {
		return origin;
	}

	public boolean setOrigin(ConnectionEnd origin) {
		boolean ok = true;
		if (this.origin == null && origin != null && origin.getNodeId() != null) {
			if (this.destinations != null && !this.destinations.isEmpty()) {
				for (ConnectionEnd ce : this.destinations) {
					if (ce.getNodeId() != null
							&& ce.getNodeId().equals(origin.getNodeId()))
						ok = false;
				}
			}
		} else {
			ok = false;
		}

		if (ok)
			this.origin = origin;
		return ok;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public List<ConnectionEnd> getDestinations() {
		return destinations;
	}

	public boolean addDestination(ConnectionEnd destination) {
		boolean ok = true;
		if (this.origin != null && this.origin.getNodeId() != null
				&& this.origin.getNodeId().equals(destination.getNodeId())) {
			ok = false;
		}
		if (ok)
			this.destinations.add(destination);
		return ok;
	}

	public boolean addDestinations(List<ConnectionEnd> destinations) {
		for (ConnectionEnd ce : destinations) {
			if (!addDestination(ce))
				return false;
		}
		return true;
	}

	public boolean hasOrigin() {
		return this.origin != null;
	}

	public boolean hasDestination() {
		return this.destinations != null && !this.destinations.isEmpty();
	}

	public boolean isInFeedback() {
		if (this.origin != null) {
			String originNode = origin.getNodeId();
			if (this.destinations != null && !this.destinations.isEmpty()) {
				for (ConnectionEnd ce : this.destinations) {
					if (originNode != null && ce.getNodeId() != null) {
						if (originNode.equals(ce.getNodeId()))
							return true;
					} else {
						return false;
					}
				}
				return false;
			}
			return false;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.connectionId + ":\nfrom:");
		if (origin != null) {
			sb.append(origin.getNodeId());
			sb.append(" with port:" + origin.getNodePortId() + "\nto:");
		} else {
			sb.append("\nto:");
		}
		if (this.destinations != null && !this.destinations.isEmpty()) {
			for (int i = 0; i < destinations.size(); i++) {
				sb.append("*");
				if (destinations.get(i) != null)
					sb.append(destinations.get(i).getNodeId());
				sb.append(" with port:" + destinations.get(i).getNodePortId()
						+ "\n");
			}

		}
		return new String(sb);
	}

	public String leadsToFlowPort() {
		// boolean hasFlowEnd = false;
		if (this.destinations != null) {
			for (ConnectionEnd ce : destinations)
				if (ce.getNodeId() == null && ce.getNodePortId() != null) {
					return ce.getNodePortId();
				}
		}
		return null;
	}

	public boolean comesFromFlowPort() {
		if (this.origin != null)
			return this.origin.getNodeId() == null;
		return false;
	}
}
