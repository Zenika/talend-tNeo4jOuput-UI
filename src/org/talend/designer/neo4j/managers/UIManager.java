package org.talend.designer.neo4j.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.designer.neo4j.Neo4jComponent;
import org.talend.designer.neo4j.data.Index;
import org.talend.designer.neo4j.data.Neo4jData;
import org.talend.designer.neo4j.data.Relationship;
import org.talend.designer.neo4j.external.data.ExternalNeo4jUIProperties;
import org.talend.designer.neo4j.ui.Neo4jUI;
import org.talend.designer.neo4j.ui.editor.MetadataColumnExt;

public class UIManager {
	
	private Neo4jUI neo4jUI;
	
	private int neo4jResponse = SWT.NONE;
	
	private final Neo4jManager neo4jManager;
	
	private ExternalNeo4jUIProperties uiProperties;
	
	public UIManager(Neo4jManager neo4jManager) {
		this.neo4jManager = neo4jManager;
	}
	
	public ExternalNeo4jUIProperties getUIProperties() {
		if (this.uiProperties == null) {
			this.uiProperties = new ExternalNeo4jUIProperties();
		}
		return uiProperties;
	}
	
	public void setUIProperties(ExternalNeo4jUIProperties uiProperties) {
		this.uiProperties = uiProperties;
	}

	public Neo4jUI getNeo4jUI() {
		return neo4jUI;
	}

	public void setNeo4jUI(Neo4jUI neo4jUI) {
		this.neo4jUI = neo4jUI;
	}

	public int getNeo4jResponse() {
		return neo4jResponse;
	}

	public void setNeo4jResponse(int neo4jResponse) {
		this.neo4jResponse = neo4jResponse;
	}
	
	public void closeNeo4j(int response, boolean fromDialog) {
		setNeo4jResponse(response);
		Composite parent = neo4jUI.getNeo4jUIParent();
		saveCurrentUiProperties();
		MetadataTable table = (MetadataTable) neo4jManager.getNeo4jComponent().getMetadataList().get(0);
		boolean hasColumns = (table != null) && (table.getListColumns() != null && table.getListColumns().size() != 0);
		List<Map<String, Object>> originalColumnDataList = neo4jManager.getNeo4jComponent().getMapList();
		List<Map<String, Object>> currentColumnDataList = getCurrentColumnData();
		boolean containsAll1 = originalColumnDataList.containsAll(currentColumnDataList);
		boolean containsAll2 = currentColumnDataList.containsAll(originalColumnDataList);
		boolean containsAll = containsAll1 && containsAll2;
		if (hasColumns && !containsAll && response == SWT.CANCEL) {
			boolean isNotSaveSetting = MessageDialog.openQuestion(parent.getShell(), "TODO Refactor", "TODO"); // TODO: Internationalize this
			if (isNotSaveSetting) {
				reductAllData();
			} else {
				response = SWT.OK;
				setNeo4jResponse(response);
			}
		}
		if (response == SWT.OK) {
			saveAllData(currentColumnDataList);
		}		
		if (parent instanceof Shell && !fromDialog) {
			((Shell)parent).close();
		}
	}
	
	private void saveAllData(List<Map<String, Object>> map){
		neo4jManager.getNeo4jComponent().setTableElementParameter(map);
	}
	
	private List<Map<String, Object>> getCurrentColumnData() {
		List<Map<String, Object>> map = new ArrayList<>();
		MetadataTable table = (MetadataTable) neo4jManager.getNeo4jComponent().getMetadataList().get(0);
		convert(neo4jManager.getNeo4jComponent(), table);
		for (IMetadataColumn col : table.getListColumns()) {
			MetadataColumnExt ext = (MetadataColumnExt) col;
			Map<String, Object> value = new HashMap<String, Object>();
			value.put(Neo4jComponent.COLUMN_NAME, ext.getLabel());
			value.put(Neo4jComponent.AUTO_INDEXED, String.valueOf(ext.getData().isAutoIndexed()));
			value.put(Neo4jComponent.INDEX_NAMES, ext.getData().inlineIndexNames());
			map.add(value);
		}
		return map;
	}
	
	private void reductAllData() {
		List<Map<String, Object>> eps = neo4jManager.getOriginEP();
		
		neo4jManager.getNeo4jComponent().setTableElementParameter(eps);
		
	}
	
	public void convert(Neo4jComponent externalNode, IMetadataTable outputMetaTable) {
		List<IMetadataColumn> exts = new ArrayList<>();
		for (IMetadataColumn column : outputMetaTable.getListColumns()) {
			if (column instanceof MetadataColumnExt) {
				exts.add(column.clone());
			} else if (column instanceof MetadataColumn) {
				MetadataColumnExt ext = new MetadataColumnExt((MetadataColumn) column);
				ext.setData(new Neo4jData());
				exts.add(ext);
			}
		}
		outputMetaTable.setListColumns(exts);
	}
	
	private void saveCurrentUiProperties() {
		ExternalNeo4jUIProperties.setWeightsMainSashForm(neo4jUI.getMainSashForm().getWeights());
		ExternalNeo4jUIProperties.setWeightsDatasFlowViewSashForm(neo4jUI.getDatasFlowViewSashForm().getWeights());
		ExternalNeo4jUIProperties.setShellMaximized(neo4jUI.getNeo4jUIParent().getShell().getMaximized());
		if (!ExternalNeo4jUIProperties.isShellMaximized()) {
			ExternalNeo4jUIProperties.setBoundsNeo4j(neo4jUI.getNeo4jUIParent().getShell().getBounds());			
		}
	}
	
}
