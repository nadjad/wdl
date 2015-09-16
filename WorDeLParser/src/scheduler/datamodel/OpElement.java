package scheduler.datamodel;

import representation.ConnectionEnd;

public class OpElement {

	private String opName;
	private String identifier;

	public OpElement(String opName, String identifier) {
		super();
		this.opName = opName;
		this.identifier = identifier;
	}

	public OpElement(ConnectionEnd end) {
		// this.opName=end.get
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((opName == null) ? 0 : opName.hashCode());
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
		OpElement other = (OpElement) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (opName == null) {
			if (other.opName != null)
				return false;
		} else if (!opName.equals(other.opName))
			return false;
		return true;
	}

}
