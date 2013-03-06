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
	
	private Neo4jData neo4jData = new Neo4jData();
	
	private Button cacheNodeCheckbox;
	private Text typeText;
	private Combo directionCombo;
	
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
		
		createCacheNodeCheckbox(sashFormRelationship);
		createRelationshipTableView(sashFormRelationship);
		
		sashFormRelationship.setWeights(new int[] {1, 2, 7});
		
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

	private void createCacheNodeCheckbox(SashForm sashFormRelationship) {
		cacheNodeCheckbox = new Button(sashFormRelationship, SWT.CHECK);
		cacheNodeCheckbox.setText("Create from cached node (Advanced option)");
		cacheNodeCheckbox.setSelection(neo4jData.getRelationships().isFromCache());
		cacheNodeCheckbox.setData(neo4jData.getRelationships().isFromCache());
		cacheNodeCheckbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				neo4jData.getRelationships().setFromCache((!neo4jData.getRelationships().isFromCache()));
				enableOrDisableRelationTable();
			}
		});
		Composite form = new Composite(sashFormRelationship, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.wrap = true;
		layout.pack = true;
		form.setLayout(layout);
		
		Label typeLabel = new Label(form, SWT.NONE);
		typeLabel.setText("Type: ");
		
		typeText = new Text(form, SWT.BORDER);
		String text = neo4jData.getRelationships().getRelationship().getType() == null ? "" : neo4jData.getRelationships().getRelationship().getType();
		typeText.setText(text);
		typeText.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent arg0) {
				neo4jData.getRelationships().getRelationship().setType(typeText.getText());
			}			
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
		
		Label directionLabel = new Label(form, SWT.NONE);
		directionLabel.setText("Direction: ");
		
		directionCombo = new Combo(form, SWT.NONE);
		directionCombo.setData(neo4jData.getRelationships().getRelationship().getDirection().getName());
		directionCombo.setItems(Relationship.Direction.getNames());
		directionCombo.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Direction direction = Direction.getFromName(directionCombo.getItem(directionCombo.getSelectionIndex()));
				neo4jData.getRelationships().getRelationship().setDirection(direction);
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	private void enableOrDisableRelationTable() {
		boolean fromCache = neo4jData.getRelationships().isFromCache();
		inputRelationshipEditor.getTable().setEnabled(!fromCache);
		((ParameterToolbarView)inputRelationshipEditor.getExtendedToolbar()).setEnable(!fromCache);
		
		typeText.setEnabled(fromCache);
		directionCombo.setEnabled(fromCache);
	}
	
	public IndexTableView getInputIndexEditor() {
		return inputIndexEditor;
	}

	public RelationshipTableView getInputRelationshipEditor() {
		return inputRelationshipEditor;
	}

	public void setNeo4jData(Neo4jData neo4jData) {
		this.neo4jData = neo4jData;
		cacheNodeCheckbox.setData(neo4jData.getRelationships().isFromCache());
		cacheNodeCheckbox.setSelection(neo4jData.getRelationships().isFromCache());
		String text = neo4jData.getRelationships().getRelationship().getType() == null ? "" : neo4jData.getRelationships().getRelationship().getType();
		typeText.setText(text);
		directionCombo.setData(neo4jData.getRelationships().getRelationship().getDirection());
		enableOrDisableRelationTable();
	}
	
}
