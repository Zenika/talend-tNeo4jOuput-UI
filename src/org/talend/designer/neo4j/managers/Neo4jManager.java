package org.talend.designer.neo4j.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.talend.designer.neo4j.Neo4jComponent;
import org.talend.designer.neo4j.ui.Neo4jUI;

public class Neo4jManager {
	
	private List<Map<String, Object>> originEP = new ArrayList<>();
	
	private Neo4jComponent neo4jComponent;
	
	private UIManager uiManager;
	
	private CommandStack commandStack;
	
	public Neo4jManager(Neo4jComponent neo4jComponent) {
		this.neo4jComponent = neo4jComponent;
		this.uiManager = new UIManager(this);
		originEP.clear();
		initOriginEP();
	}

	@SuppressWarnings("unchecked") 
	private void initOriginEP() {
		List<Map<String, Object>> lines = neo4jComponent.getMapList();
		for (Map<String, Object> map : lines) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.putAll(map);
			//copyLists(map, newMap);
			originEP.add(newMap);
		}
	}

	// TODO: Refactor to clone 
	private void copyLists(Map<String, Object> map, Map<String, Object> newMap) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof List) {
				List<Object> originalList = (List<Object>) entry.getValue();
				List<Object> copyList = new ArrayList<Object>(originalList);
				Collections.copy(copyList, originalList);
				newMap.put(entry.getKey(), copyList);
			}
		}
	}
	
	public Neo4jComponent getNeo4jComponent() {
		return neo4jComponent;
	}

	public void setNeo4jComponent(Neo4jComponent neo4jComponent) {
		this.neo4jComponent = neo4jComponent;
	}

	public UIManager getUiManager() {
		return uiManager;
	}

	public void setUiManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}

	public CommandStack getCommandStack() {
		return commandStack;
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;
	}

	public List<Map<String, Object>> getOriginEP() {
		return originEP;
	}
	
	
}
