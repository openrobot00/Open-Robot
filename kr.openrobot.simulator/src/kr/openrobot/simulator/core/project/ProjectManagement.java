package kr.openrobot.simulator.core.project;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

public class ProjectManagement{
	/**
	 * The constructor.
	 */
	private IFile file;
	private IPath path;
	private Object obj;
	public ProjectManagement() {
	}
	
	public String getProjectPath() {
		IWorkbenchWindow window = 
				PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStructuredSelection structured = 
				(IStructuredSelection)window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
		obj = structured.getFirstElement();
		if (obj instanceof IFile) {
			file = (IFile)structured.getFirstElement();
			path = file.getLocation();
		}
		return path.toPortableString();
	}
//	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		IWorkbenchPage activePage = window.getActivePage();
//		ISelection selection = activePage.getSelection();
//		if (selection != null) {
//			System.out.println("Got selection");
//			if (selection instanceof IStructuredSelection) {
//				System.out.println("Got a structured selection");
//				if (selection instanceof ITreeSelection) {
//					TreeSelection treeSelection = (TreeSelection) selection;
//					TreePath[] treePaths = treeSelection.getPaths();
//					TreePath treePath = treePaths[0];
//
//					System.out.println("Last");
//					Object lastSegmentObj = treePath.getLastSegment();
//					Class currClass = lastSegmentObj.getClass();
//					while(currClass != null) {
//						System.out.println("  Class=" + currClass.getName());
//						Class[] interfaces = currClass.getInterfaces();
//						for(Class interfacey : interfaces) {
//							System.out.println("   I=" + interfacey.getName());
//						}
//						currClass = currClass.getSuperclass();
//					}
//					if(lastSegmentObj instanceof IAdaptable) {
//						IFile file = (IFile) ((IAdaptable) lastSegmentObj).getAdapter(IFile.class);
//						if(file != null) {
//							System.out.println("File=" + file.getName());
//							String path = file.getRawLocation().toOSString();
//							System.out.println("path: " + path);
//						}
//					}
//				}
//			}
//		}
//		return null;
//	}
}