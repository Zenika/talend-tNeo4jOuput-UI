package org.talend.designer.neo4j.ui.tabs;

import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.talend.designer.neo4j.Neo4jComponent;
import org.talend.designer.neo4j.data.Neo4jData;
import org.talend.designer.neo4j.data.Relationship;
import org.talend.designer.neo4j.data.Relationship.Direction;
import org.talend.designer.neo4j.ui.Neo4jUI;
import org.talend.designer.neo4j.ui.editor.MetadataTableEditorViewExt;

public class TabFolderEditors extends CTabFolder{
	
	protected int lastSelectedTab;
	
	private IndexTableView inputIndexEditor;
	
	private RelationshipTableView inputRelationshipEditor;
	
	protected static final int VERTICAL_SPACING_FORM = 0;
	
	protected static final int WIDTH_BUTTON_PIXEL = 100;
	
	protected static final int HEIGHT_BUTTON_PIXEL = 30;
	
	private Neo4jComponent component;
		
	public TabFolderEditors(Composite parentComposite, int style, Neo4jComponent component,
			MetadataTableEditorViewExt neo4jTableEditor) {
		super(parentComposite, style);
		this.component = component;
		setSimple(false);
		setLayout(new GridLayout());
		setLayoutData(new GridData(GridData.FILL_BOTH));
		createComponents();
	}
	
	private void createComponents() {
		CTabItem item = new CTabItem(this, SWT.BORDER);
		item.setText("Index creation"); // TODO: Internationalize this
		SashForm sashFormIndex = new SashForm(this, SWT.SMOOTH | SWT.HORIZONTAL | SWT.SHADOW_OUT);
		sashFormIndex.setLayout(new RowLayout(SWT.HORIZONTAL));
		item.setControl(sashFormIndex);		
		createIndexTableView(sashFormIndex);
		
		item = new CTabItem(this, SWT.NONE);
		item.setText("Relationship creation"); // TODO: Internationalize this
		SashForm sashFormRelationship = new SashForm(this, SWT.SMOOTH | SWT.VERTICAL | SWT.SHADOW_OUT);
		sashFormRelationship.setLayout(new RowLayout(SWT.VERTICAL));
		item.setControl(sashFormRelationship);
		createRelationshipTableView(sashFormRelationship);
		
		addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				lastSelectedTab = getSelectionIndex();
			}
		});
		setSelection(0);
	}

	public void createIndexTableView(SashForm inOutMetaEditorContainer) {
		inputIndexEditor = new IndexTableView(inOutMetaEditorContainer, SWT.BORDER, component);
		inOutMetaEditorContainer.setData(inputIndexEditor);
		inputIndexEditor.setTitle("");
		inputIndexEditor.getExtendedToolbar().getAddButton().getButton().setEnabled(false);
	}

	public void createRelationshipTableView(SashForm inOutMetaEditorContainer) {
		inputRelationshipEditor = new RelationshipTableView(inOutMetaEditorContainer, SWT.BORDER, component);
		inOutMetaEditorContainer.setData(inputRelationshipEditor);
		inputRelationshipEditor.setTitle("");
		inputRelationshipEditor.getExtendedToolbar().getAddButton().getButton().setEnabled(false);
	}

	public IndexTableView getInputIndexEditor() {
		return inputIndexEditor;
	}

	public RelationshipTableView getInputRelationshipEditor() {
		return inputRelationshipEditor;
	}

}
