package org.talend.designer.neo4j.ui.tabs;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.IExtendedTablePushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

public class ParameterAddButton<T> extends AddPushButtonForExtendedTable {

	private Class<T> objectToAdd;
	
	public ParameterAddButton(Composite parent, AbstractExtendedTableViewer<T> extendedTableViewer, Class<T> objectToAdd) {
		super(parent, extendedTableViewer);
		this.objectToAdd = objectToAdd;
	}

	@Override
	protected T getObjectToAdd() {		
		try {
			return objectToAdd.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
