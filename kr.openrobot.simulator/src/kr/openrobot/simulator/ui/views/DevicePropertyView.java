package kr.openrobot.simulator.ui.views;

import kr.openrobot.simulator.model.PropertiesManager;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

public class DevicePropertyView extends ViewPart {

	private TableViewer viewer;
	private TableColumn keyColumn;
	private TableColumn valueColumn;

	public DevicePropertyView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		createTableViewer(parent);


	}


	private void createTableViewer(Composite parent) {
		viewer =
				new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
						| SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();

		TableColumnLayout layout = new TableColumnLayout();
		parent.setLayout(layout);

		keyColumn = new TableColumn(table, SWT.LEFT);
		keyColumn.setText("Name");
		layout.setColumnData(keyColumn, new ColumnWeightData(4));

		valueColumn = new TableColumn(table, SWT.LEFT);
		valueColumn.setText("Location");
		layout.setColumnData(valueColumn, new ColumnWeightData(9));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new PropertiesViewContentProvider());
		viewer.setLabelProvider(new PropertiesViewLabelProvider());
		viewer.setInput(PropertiesManager.getManager());

		getSite().setSelectionProvider(viewer);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
