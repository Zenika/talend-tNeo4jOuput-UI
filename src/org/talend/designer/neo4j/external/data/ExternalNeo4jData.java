package org.talend.designer.neo4j.external.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExternalNeo4jData implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ExternalNeo4jTable> outputTables = new ArrayList<>();
	
	private ExternalNeo4jUIProperties uiProperties = new ExternalNeo4jUIProperties();

	public List<ExternalNeo4jTable> getOutputTables() {
		return outputTables;
	}

	public void setOutputTables(List<ExternalNeo4jTable> outputTables) {
		this.outputTables = outputTables;
	}

	public ExternalNeo4jUIProperties getUiProperties() {
		return uiProperties;
	}

	public void setUiProperties(ExternalNeo4jUIProperties uiProperties) {
		this.uiProperties = uiProperties;
	}
	
}
