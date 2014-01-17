package kr.openrobot.simulator.ui.actions;

import kr.openrobot.simulator.core.resources.DeviceResources;
import kr.openrobot.simulator.xml.XMLParser;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SimulatorRun implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	XMLParser parser = new XMLParser();

	/**
	 * The constructor.
	 */
	public SimulatorRun() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		//IPath path = Activator.getDefault().getStateLocation().append("Simulator")

		try{
			parser.readXMLFile();

			//MessageDialog.openError(window.getShell(), "debug", "/home/yeongchann/Simulator " + DeviceResources.upAngle + " " + DeviceResources.downAngle + " " + DeviceResources.angleSpeed + " textures");
			Runtime.getRuntime().exec("/home/yeongchann/Simulator " + Integer.toString(DeviceResources.downAngle) + " " + Integer.toString(DeviceResources.upAngle) + " " + Double.toString(DeviceResources.angleSpeed) + " textures");
			//System.out.println("/home/yeongchann/Simulator " + Integer.toString(DeviceResources.upAngle) + " " + Integer.toString(DeviceResources.downAngle) + " " + Double.toString(DeviceResources.angleSpeed) + " textures");

			//Runtime.getRuntime().exec(path.toString() + " " + resource.upAngle + " " + resource.downAngle + " " + resource.angleSpeed + " textures");
			
			//Runtime.getRuntime().exec("/home/yeongchann/Simulator " + "-90" + " " + "90" + " " + "2" + " textures");
		}
		catch(Exception e){
			e.printStackTrace();
			MessageDialog.openError(window.getShell(), "SimulationTool", e.toString());
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}