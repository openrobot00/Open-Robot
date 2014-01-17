package kr.openrobot.simulator.ui.actions;

import kr.openrobot.simulator.core.codegenerator.CodeGenerate;
import kr.openrobot.simulator.xml.XMLParser;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CodeGenRun implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private XMLParser parser;
	private CodeGenerate codeGen;
	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject("test");
		IPath path = project.getFullPath();

		//workspace.addResourceChangeListener(IResourceChangedListener);
		//IProject project = root.getProjects().
		//resource = new DeviceResources();
		//at a later stage we can get this xml from a remote source
		String fullpath = workspace.getRoot().getLocation().toString() + path.toString();


		codeGen = new CodeGenerate();
		try {
			codeGen.CodeGen();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
