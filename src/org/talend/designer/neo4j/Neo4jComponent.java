package org.talend.designer.neo4j;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.SystemException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadata;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.INode;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.neo4j.external.data.ExternalNeo4jData;
import org.talend.designer.neo4j.external.data.ExternalNeo4jTable;

public class Neo4jComponent extends AbstractExternalNode {
	
	public static final String COLUMN_NAME = "SCHEMA_COLUMN";
	
	public static final String AUTO_INDEXED = "AUTO_INDEXED";
	
	public static final String CACHE_NODE = "CACHE_NODE";
	
	public static final String INDEX_NAMES = "INDEX_NAMES";
	
	private Neo4jMain neo4jMain; 
	
	private List<IMetadataTable> metadataListOut;
	
	private ExternalNeo4jData externalData;

	@Override
	public void initialize() {
		initNeo4jMain();
	}

	private void initNeo4jMain() {
		neo4jMain= new Neo4jMain(this);
	}
	
	@Override
	public int open(Display display) {
		initNeo4jMain();
		neo4jMain.createModelFromExternalData(getIODataComponents(), getMetadataList(), externalData, true);
		Shell shell = neo4jMain.createUI(display);
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				if (Neo4jMain.isStandAloneMode()){
					e.printStackTrace();
				} else {
					ExceptionHandler.process(e);
				}
			}
		}
		if (Neo4jMain.isStandAloneMode()) {
			display.dispose();
		}
		neo4jMain.buildExternalData();
		return neo4jMain.getMapperDialogResponse();
	}

	@Override
	public int open(Composite parent) {
		initNeo4jMain();
		neo4jMain.createModelFromExternalData(getIODataComponents(), getMetadataList(), externalData, true);
		Dialog dialog = neo4jMain.createNeo4jDialog(parent.getShell());
		dialog.open();
		return neo4jMain.getMapperDialogResponse();
	}

	@Override
	public void setExternalData(IExternalData persistentData) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void renameInputConnection(String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameOutputConnection(String oldName, String newName) {
		if (oldName == null || newName == null) {
			throw new NullPointerException();
		}
		if (externalData != null) {
			List<ExternalNeo4jTable> outputTables = externalData.getOutputTables();
			for (ExternalNeo4jTable table : outputTables) {
				if (table.getName().equals(oldName)) {
					table.setName(newName);
					break;
				}
			}
		}
	}

	@Override
	public IComponentDocumentation getComponentDocumentation(
			String componentName, String tempFolderPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExternalData getTMapExternalData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void renameMetadataColumnName(String conectionName,
			String oldColumnName, String newColumnName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IMetadataTable> getMetadataList() {
		return metadataListOut;
	}
	
	@Override
	public void setMetadataList(List<IMetadataTable> metadataTablesOut) {
		boolean needUpdateMetadata = false;
		if (metadataListOut == null) {
			needUpdateMetadata = true;
		} else if (metadataListOut.size() != metadataTablesOut.size()) {
			needUpdateMetadata = true;
		} else {
			for (IMetadataTable metadataTable : metadataListOut) {
				for (IMetadataTable externalMetaTable : metadataTablesOut) {
					if (!metadataTable.sameMetadataAs(externalMetaTable, IMetadataColumn.OPTIONS_NONE)) {
						needUpdateMetadata = true;
						break;
					}
				}
				if (needUpdateMetadata) {
					break;
				}
			}
		}
		if (needUpdateMetadata) {
			this.metadataListOut = metadataTablesOut;
		}
	}
	
	@Override
	public IODataComponentContainer getIODataComponents() {
		IODataComponentContainer inAndOut = new IODataComponentContainer();
		
		List<IODataComponent> outputs = inAndOut.getOuputs();
		for (IConnection currentConnection : getOutgoingConnections()) {
			if (currentConnection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
				IODataComponent component = new IODataComponent(currentConnection, metadataListOut.get(0));
				outputs.add(component);
			}
		}
		List<IODataComponent> inputs = inAndOut.getInputs();
		for (IConnection currentConnection : getIncomingConnections()) {
			if (currentConnection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
				IODataComponent component = new IODataComponent(currentConnection, metadataListOut.get(0));
				inputs.add(component);
			}
		}
		return inAndOut;
	}
//	
//	@Override
//	public void metadataOutputChanged(IODataComponent )
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMapList() {
		List<Map<String, Object>> map = new ArrayList<>();
		List<IElementParameter> eps = (List<IElementParameter>) getElementParameters();
		if (eps == null) {
			return map;
		}
		for (IElementParameter parameter : eps) {
			if (parameter.getFieldType() == EParameterFieldType.TABLE) {
				map = (List<Map<String, Object>>) parameter.getValue();
				break;
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public void setTableElementParameter(List<Map<String, Object>> epsl) {
		List<IElementParameter> eps = (List<IElementParameter>) getElementParameters();
		for (int i = 0; i < eps.size(); i++) {
			IElementParameter parameter = eps.get(i);
			if (parameter.getFieldType() == EParameterFieldType.TABLE) {
				List<Map<String, Object>> tableValues = epsl;
				List<Object> newValues = new ArrayList<>();
				for (Map<String, Object> map : tableValues) {
					Map<String, Object> newMap = new HashMap<String, Object>();
					newMap.putAll(map);
					newValues.add(newMap);
				}
				parameter.setValue(newValues);
				break;
			}
		}
	}
	
}
