package kr.openrobot.simulator.ui.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kr.openrobot.simulator.model.IPropertiesItem;

public class PropertiesViewLabelProvider extends LabelProvider
implements ITableLabelProvider {

	@Override
	public String getColumnText(Object obj, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0: // Type column
			return "";
		case 1: // Name column
			if (obj instanceof IPropertiesItem)
				return ((IPropertiesItem) obj).getName();
			if (obj != null)
				return obj.toString();
			return "";
		case 2: // Location column
			if (obj instanceof IPropertiesItem)
				return ((IPropertiesItem) obj).getLocation();
			return "";
		default:
			return "";
		}
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
//	public void createColumns(TableViewer viewer) {
//		Table table = viewer.getTable();
//		//grab the first node and figure out the labels and bounds
//		Node node = PropertyViewCollection.getInstance().getEntityList().get(0).getNode();
//		NodeList subNodes = (NodeList)node.getChildNodes();
//		buildColumnHeadings(viewer, subNodes);
//
//	}
//
//	private void buildColumnHeadings(TableViewer viewer, NodeList subNodes) {
//		int l = 0;
//		for (int j = 0; j < subNodes.getLength(); j++) {
//			Node subNode = subNodes.item(j);
//			if (!"#text".equalsIgnoreCase(subNode.getNodeName())) {
//				TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
//				column.getColumn().setText(subNode.getAttributes().getNamedItem("columnLabel").getTextContent());
//				column.getColumn().setWidth(Integer.parseInt(subNode.getAttributes().getNamedItem("columnBounds").getTextContent()));
//				column.getColumn().setResizable(true);
//				l++;
//			}
//		}
//	}
//
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//		try {
//			Row e = (Row) element;
//			NodeList subNodes = e.getNode().getChildNodes();
//
//			for (int j = 0; j < subNodes.getLength(); j++) {
//				Node subNode = subNodes.item(j);
//				if (!"#text".equalsIgnoreCase(subNode.getNodeName())) {
//					if (new Integer(columnIndex).toString().equals(subNode.getNodeValue())) {
//						return subNode.getTextContent();
//					}
//				}
//			}		
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "";
//	}
//	
//}


