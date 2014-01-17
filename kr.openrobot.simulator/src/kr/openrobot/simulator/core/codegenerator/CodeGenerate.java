package kr.openrobot.simulator.core.codegenerator;
//package kr.openrobot.simulator.core.codegenerator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import kr.openrobot.simulator.Activator;
import kr.openrobot.simulator.core.resources.DeviceResources;
import kr.openrobot.simulator.xml.XMLParser;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class CodeGenerate implements IProgressMonitor, Runnable {

	XMLParser parser;

	public void CodeGen() throws CoreException  {

		IProgressMonitor monitor = null;

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("test");
		IFolder folder = project.getFolder("src");
		//		//System.out.println("/home/yeongchann/workspace"+ File.separator + project.getFullPath().toString() + File.separator + "src" +  File.separator + .c");
		//		IPath path = Path.fromOSString(/*root.getFullPath().toString()*/"/home/yeongchann/workspace"+ File.separator + project.getFullPath().toString() + File.separator + "main.c");
		//		IContainer container = (IContainer)project;
		//		System.out.println(container.getFullPath().toString());
		IFile sourceFile = folder.getFile("main.c");
		//		System.out.println(sourceFile.getFullPath().toString());
		sourceFile.create(openContentStream(), true, monitor);
		if (sourceFile.exists())
			MessageDialog.openInformation(new Shell(), "Code Generate",
					"Generate Success!\n" + "Create File: src/main.c");
		else
			MessageDialog.openError(new Shell(), "Error", "Generate Failed");
	}

	public InputStream openContentStream() throws CoreException {
		/* We want to be truly OS-agnostic */
		final String newline = System.getProperty("line.separator");
		//		parser = new XMLParser();
		//		parser.readXMLFile();

		String line, tmp;
		StringBuffer sb = new StringBuffer();
		XMLParser parser = new XMLParser();
		parser.readXMLFile();
		try {
			InputStream input = Activator.class.getResourceAsStream(
					"templates/main.c");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			try {

				while ((line = reader.readLine()) != null) {
					line = line.replaceAll("\\$\\{servo_function\\}", "");
					
//					line = line.replaceAll("\\$\\{servo_init\\}", DeviceResources.deviceName +"_init()");
//					line = line.replaceAll("\\$\\{servo_run\\}", DeviceResources.deviceName +"_run()");
					line = line.replaceAll("\\$\\{servo_init\\}", " ");
					line = line.replaceAll("\\$\\{servo_run\\}", " ");
					line = line.replaceAll("\\$\\{devName\\}", DeviceResources.deviceName);
					line = line.toString();
					sb.append(line);
					sb.append(newline);
					
					if(DeviceResources.servoEnable == true) {
						DeviceResources.servoEnable = false;
						
						InputStream input2 = Activator.class.getResourceAsStream(
								"templates/servo_function.c");
						BufferedReader reader2 = new BufferedReader(new InputStreamReader(
								input2));
							line = reader.readLine();
							line = line.toString();
							sb.append(line);
							sb.append(newline);

						while((line = reader2.readLine()) != null) {
							line = line.replaceAll("\\$\\{devName\\}", DeviceResources.deviceName);
							line = line.replaceAll("\\$\\{devUpAngle\\}", Integer.toString(DeviceResources.upAngle));
							line = line.replaceAll("\\$\\{devDownAngle\\}", Integer.toString(Math.abs(DeviceResources.downAngle)));
							line = line.toString();
							sb.append(line);
							sb.append(newline);
						}
						
					}


				}

			} finally {
				reader.close();
			}

		} catch (IOException ioe) {
			IStatus status = new Status(IStatus.ERROR, "NewFileWizard", IStatus.OK,
					ioe.getLocalizedMessage(), null);
			throw new CoreException(status);
		}

		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginTask(String name, int totalWork) {
		// TODO Auto-generated method stub

	}

	@Override
	public void done() {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalWorked(double work) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subTask(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void worked(int work) {
		// TODO Auto-generated method stub

	}
}
