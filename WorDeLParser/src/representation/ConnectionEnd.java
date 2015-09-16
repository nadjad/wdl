package representation;

import java.io.Serializable;

public class ConnectionEnd implements Serializable {

	private String NodeId;
	private String NodePortId;
	private Type type;

	public ConnectionEnd(String nodeId, String nodePortId, Type type) {
		super();
		NodeId = nodeId;
		NodePortId = nodePortId;
	}

	public String getNodeId() {
		return NodeId;
	}

	public String getNodePortId() {
		return NodePortId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((NodeId == null) ? 0 : NodeId.hashCode());
		result = prime * result
				+ ((NodePortId == null) ? 0 : NodePortId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return NodeId + ":" + NodePortId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectionEnd other = (ConnectionEnd) obj;
		if (NodeId == null) {
			if (other.NodeId != null)
				return false;
		} else if (!NodeId.equals(other.NodeId))
			return false;
		if (NodePortId == null) {
			if (other.NodePortId != null)
				return false;
		} else if (!NodePortId.equals(other.NodePortId))
			return false;
		return true;
	}

}
