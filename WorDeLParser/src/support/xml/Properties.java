package support.xml;

public class Properties {
	private String basePath;
	private String resultPath;

	public Properties() {
		this.basePath = "";
		this.resultPath = "";
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

}
