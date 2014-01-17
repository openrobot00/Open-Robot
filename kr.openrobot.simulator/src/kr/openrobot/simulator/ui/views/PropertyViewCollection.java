package kr.openrobot.simulator.ui.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import kr.openrobot.simulator.core.project.ProjectManagement;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.jdom2.*;

public class PropertyViewCollection {
	private static PropertyViewCollection content;
	private List<Row> propertyCollection;
	public static XPath xpath = XPathFactory.newInstance().newXPath();

	ProjectManagement prjManage;
	private PropertyViewCollection() {
		propertyCollection = new ArrayList<Row>();
		
		prjManage = new ProjectManagement();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		//workspace.addResourceChangeListener(IResourceChangedListener);
		IProject project = root.getProject("test");
		//IProject project = root.getProjects().
		IPath path = project.getFullPath();
		
		//at a later stage we can get this xml from a remote source
		String documentName = workspace.getRoot().getLocation().toString() + path.toString() + File.separator + "test.xml"; 
		System.out.println(documentName);
		String expression = "/RowCollection/Row";
		try {

			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = parser.parse(new File(documentName));
			NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);			
			Row e;
			for (int i = 0, n = nodes.getLength(); i < n; i++) {
				Node node = nodes.item(i);
				e = new Row();
				e.setNode(node);
				propertyCollection.add(e);

			}


		}
		catch (Exception e) {
			// we still want the app to start up
			e.printStackTrace();
		}			


	}

	public static synchronized PropertyViewCollection getInstance() {
		if (content != null) {
			return content;
		} else {
			content = new PropertyViewCollection();
			return content;
		}
	}

	public List<Row> getEntityList() {
		return propertyCollection;
	}
}
