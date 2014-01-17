package kr.openrobot.simulator.ui.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class DownloadAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		Runtime runtime = Runtime.getRuntime();
		Process process;
		StringWriter writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);


		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject("test");
		IPath path = project.getFullPath();
		String fullpath = workspace.getRoot().getLocation().toString() + path.toString();

		try {

			MessageConsole console = new MessageConsole("BuildConsole", null);
			//MessageConsole console = Activator.getDefault().getConsole("AVRDude");
			MessageConsoleStream stream = console.newMessageStream();
			console.clearConsole();
			console.activate();
			process = runtime.exec("avrdude -p m128 -c avr910 -b 115200 -P /dev/ttyUSB0 -U flash:w:"
					+ fullpath+File.separator+"bin"+File.separator+"main.hex:i");
			//			System.out.println("avrdude -p m128 -c avr910 -b 115200 -P /dev/ttyUSB0 -U flash:w:"
			//					+ fullpath+File.separator+"bin"+File.separator+"main.hex:i >& "
			//					+ fullpath+File.separator+"bin"+File.separator+"down.log");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				print.write(line + "\n");
				print.flush();
			}
			stream.println(writer.toString());
			System.out.println(writer.toString());
			int ret = process.waitFor();
			if(ret == 0)
				MessageDialog.openInformation(new Shell(), "AVRDude",
						"Download Success.\n");
			else
				MessageDialog.openError(new Shell(), "Error", "Download Failed.");
			
			System.out.println("return" + ret);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//workspace.addResourceChangeListener(IResourceChangedListener);
		//IProject project = root.getProjects().
		//resource = new DeviceResources();
		//at a later stage we can get this xml from a remote source

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
