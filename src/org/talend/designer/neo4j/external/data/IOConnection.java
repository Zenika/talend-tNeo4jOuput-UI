package org.talend.designer.neo4j.external.data;

import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;

public class IOConnection {
	
	private String name;
	
	private IMetadataTable table;
	
	private EConnectionType connectionType;
	
	public IOConnection(IConnection connection) {
		this.name = connection.getName();
		this.table = connection.getMetadataTable();
		this.connectionType = connection.getLineStyle();
	}
	
	public IOConnection(IODataComponent ioDataComponent) {
		this.name = ioDataComponent.getName();
		this.table = ioDataComponent.getTable();
		this.connectionType = ioDataComponent.getConnectionType();
	}

	public String getName() {
		return name;
	}

	public IMetadataTable getTable() {
		return table;
	}

	public EConnectionType getConnectionType() {
		return connectionType;
	}
}
