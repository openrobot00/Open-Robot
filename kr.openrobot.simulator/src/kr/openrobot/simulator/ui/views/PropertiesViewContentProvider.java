package kr.openrobot.simulator.ui.views;

import kr.openrobot.simulator.model.PropertiesManager;
import kr.openrobot.simulator.model.PropertiesManagerEvent;
import kr.openrobot.simulator.model.PropertiesManagerListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class PropertiesViewContentProvider
	implements IStructuredContentProvider, PropertiesManagerListener {
	
	private TableViewer tableViewer;
	private PropertiesManager manager;
	
	@Override
	public Object[] getElements(Object inputElement) {
//		PropertyViewCollection entityList = PropertyViewCollection.getInstance();
//		return entityList.getEntityList().toArray();
		return manager.getProperties();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.tableViewer = (TableViewer)viewer;
		if(manager != null)
			manager.removePropertiesManagerListener(this);
		manager = (PropertiesManager)newInput;
		if(manager != null)
			manager.addPropertiesManagerListener(this);
	}

	@Override
	public void PropertiesChanged(PropertiesManagerEvent event) {
		// TODO Auto-generated method stub
		tableViewer.getTable().setRedraw(false);
		try {
			tableViewer.remove(event.getItemsRemoved());
			tableViewer.add(event.getItemsAdded());
		} finally {
			tableViewer.getTable().setRedraw(true);
		}
	}
}
