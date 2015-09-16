package support;

public class Error {
	private String message;
	private String type;
	private String representation;
	private int instanceId;

	public Error(String message, String type) {
		super();
		this.message = message;
		this.type = type;
		this.representation = "Error" + "\n\ttype: " + type + "\n\t" + message;
	}

	public String getMessage() {
		return message;
	}

	public void setId(Integer id) {
		this.instanceId = id;
		this.representation = "Error: " + instanceId + "\n     type: " + type
				+ "\n     " + message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return this.representation;
	}
}
