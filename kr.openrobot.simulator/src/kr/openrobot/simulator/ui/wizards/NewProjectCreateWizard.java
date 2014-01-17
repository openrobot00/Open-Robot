/**
 * 
 */
package kr.openrobot.simulator.ui.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import kr.openrobot.simulator.Activator;
import kr.openrobot.simulator.xml.XMLParser;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.jdom2.Element;
import org.jdom2.JDOMException;



/**
 * Creates a new project containing folders and files for use with an
 * Example.com enterprise web site.
 * 
 * @author Nathan A. Good &lt;mail@nathanagood.com&gt;
 * 
 */
public class NewProjectCreateWizard extends Wizard implements INewWizard,
IExecutableExtension {

	/*
	 * Use the WizardNewProjectCreationPage, which is provided by the Eclipse
	 * framework.
	 */
	private WizardNewProjectCreationPage wizardPage;
	//private NewCProjectWizardPage wizardPage;
	private NewDevicePropertiesPage propertiesPage;
	private NewObjectSelectionPage objSelectPage;
	private IConfigurationElement config;
	private XMLParser parser = new XMLParser();

	private IWorkbench workbench;

	private IStructuredSelection selection;

	private IProject project;
	public List devInfoList;
	public List propertiesList;

	/**
	 * Constructor
	 */
	public NewProjectCreateWizard() {
		super();
	}

	public void addPages() { //처음 페이지에서 finish버튼 활성화됨.
		/*
		 * Unlike the custom new wizard, we just add the pre-defined one and
		 * don't necessarily define our own.
		 */
		wizardPage = new WizardNewProjectCreationPage(
				"NewExampleComSiteProject");

		wizardPage.setDescription("Create a new Robot Simulator Project." +
				"\nIf you finish this page, Project set default(Single Joint Arm).");
		wizardPage.setTitle("New Robot Simulator Project");
		objSelectPage = new NewObjectSelectionPage(selection);
		propertiesPage = new NewDevicePropertiesPage(selection);

		addPage(wizardPage);
		addPage(objSelectPage);
		objSelectPage.getNextPage();
		addPage(propertiesPage);
	}



	@Override
	public boolean performFinish() {

		if (project != null) {
			return true;
		}

		final IProject projectHandle = wizardPage.getProjectHandle();

		URI projectURI = (!wizardPage.useDefaults()) ? wizardPage
				.getLocationURI() : null;

				IWorkspace workspace = ResourcesPlugin.getWorkspace();

				final IProjectDescription desc = workspace
						.newProjectDescription(projectHandle.getName());

				desc.setLocationURI(projectURI);

				/*
				 * Just like the NewFileWizard, but this time with an operation object
				 * that modifies workspaces.
				 */
				WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
					protected void execute(IProgressMonitor monitor)
							throws CoreException {
						createProject(desc, projectHandle, monitor);
					}
				};

				/*
				 * This isn't as robust as the code in the BasicNewProjectResourceWizard
				 * class. Consider beefing this up to improve error handling.
				 */
				try {
					getContainer().run(true, true, op);
				} catch (InterruptedException e) {
					return false;
				} catch (InvocationTargetException e) {
					Throwable realException = e.getTargetException();
					MessageDialog.openError(getShell(), "Error", realException
							.getMessage());
					return false;
				}

				project = projectHandle;

				if (project == null) {
					return false;
				}

				BasicNewProjectResourceWizard.updatePerspective(config);
				BasicNewProjectResourceWizard.selectAndReveal(project, workbench
						.getActiveWorkbenchWindow());



				return true;
	}

	/**
	 * This creates the project in the workspace.
	 * 
	 * @param description
	 * @param projectHandle
	 * @param monitor
	 * @throws CoreException
	 * @throws OperationCanceledException
	 * @throws JDOMException 
	 * @throws IOException 
	 */
	void createProject(IProjectDescription description, IProject proj,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		//codeGen = new CodeGenerate();

		try {
			monitor.beginTask("", 2000);

			proj.create(description, new SubProgressMonitor(monitor, 1000));

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1000));

			/*
			 * Okay, now we have the project and we can do more things with it
			 * before updating the perspective.
			 */
			IContainer container = (IContainer) proj;


			addFileToProject(container, new Path("DeviceInfo.xml"),
					openContentStream("DeviceInfo"), monitor);
			addFileToProject(container, new Path("DeviceProperties.xml"),
					openContentStream("DeviceProperties"), monitor);
			addFileToProject(container, new Path("ProjectProperties.xml"),
					openContentStream("ProjectProperties"), monitor);
			
			/* Add the style folder and the site.css file to it */
			final IFolder srcFolder = container.getFolder(new Path("src"));
			srcFolder.create(true, true, monitor);
			final IFolder binFolder = container.getFolder(new Path("bin"));
			binFolder.create(true, true, monitor);
			//parser.readXMLFile();




			/*
			 * Add the images folder, which is an official Exmample.com standard
			 * for static web projects.
			 */

		} finally { 
			monitor.done();
		}
	}


	/**
	 * Initialize the file contents to contents of the given resource.
	 */
	private InputStream openContentStream(String title) throws CoreException {


		final String newline = System.getProperty("line.separator");

		String line;
		StringBuffer sb = new StringBuffer();
		try {
			InputStream input = Activator.class.getResourceAsStream(
					"templates/" + title + ".resource");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			try {

				while ((line = reader.readLine()) != null) {
					// DeviceInfo.xml replace
					if (title == "DeviceInfo") {
						System.out.println(objSelectPage.devType);
						line = line.replaceAll("\\$\\{devType\\}", objSelectPage.devType);
						System.out.println(objSelectPage.devName);
						line = line.replaceAll("\\$\\{devName\\}", objSelectPage.devName);
						System.out.println(objSelectPage.devTimer);
						line = line.replaceAll("\\$\\{devTimer\\}", objSelectPage.devTimer);
						System.out.println(objSelectPage.devUpAngle);
						line = line.replaceAll("\\$\\{devUpAngle\\}", objSelectPage.devUpAngle);
						System.out.println(objSelectPage.devDownAngle);
						line = line.replaceAll("\\$\\{devDownAngle\\}", '-' + objSelectPage.devDownAngle);
						System.out.println(objSelectPage.devSpeed);
						line = line.replaceAll("\\$\\{devSpeed\\}", objSelectPage.devSpeed);
					}
					if (title == "DeviceProperties") {
						// DeviceProperties.xml
						System.out.println(propertiesPage.jointTouque);
						line = line.replaceAll("\\$\\{jointTouque\\}", propertiesPage.jointTouque);
						System.out.println(propertiesPage.jointRPM);
						line = line.replaceAll("\\$\\{jointRPM\\}", propertiesPage.jointRPM);
						System.out.println(propertiesPage.jointResolution);
						line = line.replaceAll("\\$\\{jointResolution\\}", propertiesPage.jointResolution);
						System.out.println(propertiesPage.jointVoltage);
						line = line.replaceAll("\\$\\{jointVoltage\\}", propertiesPage.jointVoltage);
						System.out.println(propertiesPage.linkLength);
						line = line.replaceAll("\\$\\{linkLength\\}", propertiesPage.linkLength);
						System.out.println(propertiesPage.linkMass);
						line = line.replaceAll("\\$\\{linkMass\\}", propertiesPage.linkMass);
						System.out.println(objSelectPage.devMode);
						line = line.replaceAll("\\$\\{devMode\\}", objSelectPage.devMode);
					}
					if (title == "ProjectProperties") {
						// projectProperties.xml
						line = line.replaceAll("\\$\\{mcuType\\}", objSelectPage.mcuType);
						line = line.replaceAll("\\$\\{mcuPort\\}", objSelectPage.mcuPort);
						line = line.replaceAll("\\$\\{mcuTimerPort\\}", objSelectPage.mcuTimerPort);
					}
					sb.append(line);
					sb.append(newline);
				}

			} finally {
				reader.close();
			}

		} catch (IOException ioe) {
			IStatus status = new Status(IStatus.ERROR, "ExampleWizard", IStatus.OK,
					ioe.getLocalizedMessage(), null);
			throw new CoreException(status);
		}

		return new ByteArrayInputStream(sb.toString().getBytes());


	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

	/**
	 * Sets the initialization data for the wizard.
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.config = config;
	}

	/**
	 * Adds a new file to the project.
	 * 
	 * @param container
	 * @param path
	 * @param contentStream
	 * @param monitor
	 * @throws CoreException
	 */
	private void addFileToProject(IContainer container, Path path,
			InputStream contentStream, IProgressMonitor monitor)
					throws CoreException {
		final IFile file = container.getFile(path);
		System.out.println(file.getFullPath().toString());

		if (file.exists()) {
			file.setContents(contentStream, true, true, monitor);
		} else {
			file.create(contentStream, true, monitor);
		}
	}
}
