package kr.openrobot.simulator.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import kr.openrobot.simulator.core.resources.DeviceResources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class XMLParser {

	SAXBuilder builder = new SAXBuilder();
	Document xmlDoc;
	

	int cnt = 0;
	DeviceResources resource = new DeviceResources();
	//List root
	public void readXMLFile() {
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IProject project = root.getProject("test");
			IPath path = project.getFullPath();
			
			//workspace.addResourceChangeListener(IResourceChangedListener);
			//IProject project = root.getProjects().
			resource = new DeviceResources();
			//at a later stage we can get this xml from a remote source
			String documentName = workspace.getRoot().getLocation().toString() + path.toString() + File.separator + "DeviceInfo.xml";
			System.out.println(workspace.getRoot().getLocation().toString() + path.toString() + File.separator + "DeviceInfo.xml");
			// TODO Auto-generated constructor stub
			//IFile xmlFile = ((IFileEditorInput) input).getFile();


			xmlDoc = builder.build(documentName);

			Element rootElement = xmlDoc.getRootElement();
			DeviceResources.simulType = rootElement.getAttributeValue("type");
			if(rootElement.getAttributeValue("type") == "Single Joint Arm")
				System.out.println("!!");
				SingleJointParse(rootElement);
			DeviceResources.deviceCount = ++cnt;

//			List list2 = childElement.getChildren();
//			Iterator it2 = list2.iterator();
//
//			for(int i = 0; i < list2.size(); i++) {
//				childElement2 = (Element)it2.next();
//				//			System.out.println(childElement2.getName());
//				//			System.out.println(childElement2.getValue());
//				if (childElement2.getName() == "DeviceName")
//					DeviceResources.deviceName[cnt] = childElement2.getValue();
//				else if(childElement2.getName() == "Timertype")
//					DeviceResources.timer[cnt] = childElement2.getValue();
//
//			}
//			List list3 = childElement3.getChildren();
//			Iterator it3 = list3.iterator();
//			for(int i = 0; i < list3.size(); i++) {
//				childElement3 = (Element)it3.next();
//				//			System.out.println(childElement2.getName());
//				//			System.out.println(childElement2.getValue());
//				if (childElement3.getName() == "UpDegree")
//					DeviceResources.upAngle[cnt] = Integer.parseInt(childElement3.getValue());
//				else if(childElement3.getName() == "DownDegree")
//					DeviceResources.downAngle[cnt] = Integer.parseInt(childElement3.getValue());
//				else if(childElement3.getName() == "Speed")
//					DeviceResources.angleSpeed[cnt] = Integer.parseInt(childElement3.getValue());
//
//			}
//			++cnt;
//			resource.deviceCount = cnt;
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SingleJointParse(Element rootElement) {
		List list = rootElement.getChildren();
		Iterator it = list.iterator();
		
		Element childElement = (Element)it.next();
		DeviceResources.servoEnable = true;
		DeviceResources.deviceType = childElement.getAttributeValue("type");
		DeviceResources.deviceName = childElement.getAttributeValue("Name");
		DeviceResources.timer = childElement.getAttributeValue("TimerType");
		DeviceResources.upAngle = Integer.parseInt(childElement.getAttributeValue("UpAngle"));
		System.out.println(childElement.getAttributeValue("UpAngle"));
		DeviceResources.downAngle = Integer.parseInt(childElement.getAttributeValue("DownAngle"));
		System.out.println(childElement.getAttributeValue("DownAngle"));
		DeviceResources.angleSpeed = Integer.parseInt(childElement.getAttributeValue("Speed"));
		System.out.println(childElement.getAttributeValue("Speed"));
			
	}
	public void SixJointParse(Element rootElement) {
		List list = rootElement.getChildren();
		Iterator it = list.iterator();
		DeviceResources.deviceCount = cnt;
		Element childElement = (Element)it.next();
		DeviceResources.servoEnable = true;
		DeviceResources.deviceType = childElement.getAttributeValue("type");
		DeviceResources.deviceName = childElement.getAttributeValue("Name");
		DeviceResources.timer = childElement.getAttributeValue("TimerType");
		DeviceResources.upAngle = Integer.parseInt(childElement.getAttributeValue("UpAngle"));
		DeviceResources.downAngle = Integer.parseInt(childElement.getAttributeValue("downAngle"));
		DeviceResources.angleSpeed = Integer.parseInt(childElement.getAttributeValue("Speed"));
			
	}
	
	public Document addContent(Element element) {
		return xmlDoc.addContent(element);
	}
	
	public Element addInnerElement(Element element, String elementKey, String elementValue) {
		Element newElement = new Element(elementKey);
		newElement.setText(elementValue);
		element.addContent(newElement);
		return newElement;
	}

	public void getRootElement() {
		// TODO Auto-generated method stub
		
	}

}
