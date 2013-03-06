package org.talend.designer.neo4j.ui.tabs;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.CopyPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.core.ui.metadata.editor.MetadataToolbarEditorView;

public abstract class ParameterToolbarView<T> extends ExtendedToolbarView {
	
	public ParameterToolbarView(Composite parent, int style,
			AbstractExtendedTableViewer<T> extendedTableViewer) {
		super(parent, style, extendedTableViewer);
		
	}

	@Override
	protected abstract AddPushButton createAddPushButton();
	
	@Override
	protected CopyPushButton createCopyPushButton() {		
		return null;
	}
	
	@Override
	public PastePushButton createPastePushButton() {
		return null;
	}
	
	@Override
	public ImportPushButton createImportPushButton() {
		return null;
	}
	
	public void setEnable(boolean enabled) {
		for (ExtendedPushButton button : getButtons()) {
			if (button != null && button.getButton() != null) {
				button.getButton().setEnabled(enabled);
			}
		}
	}
}
