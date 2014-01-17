package kr.openrobot.simulator;

import java.io.PrintStream;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.Workbench;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class Perspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public Perspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		// Print Console View
		MessageConsole console = new MessageConsole("console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
		MessageConsoleStream stream = console.newMessageStream();
		// Read print
		stream.setColor(new Color(Workbench.getInstance().getDisplay(), 252, 61, 61));
		
		PrintStream myStream = new PrintStream(stream);
		System.setOut(myStream);
		System.setErr(myStream);
		
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.

		IFolderLayout bottom =
				factory.createFolder(
						"bottomRight", //NON-NLS-1
						IPageLayout.BOTTOM,
						0.75f,
						factory.getEditorArea());
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		bottom.addView("de.innot.avreclipse.views.AVRDeviceView");
		bottom.addView("de.innot.avreclipse.views.supportedmcus");
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout topLeft =
				factory.createFolder(
						"topLeft", //NON-NLS-1
						IPageLayout.LEFT,
						0.25f,
						factory.getEditorArea());
		//topLeft.addView(IPageLayout.ID_RES_NAV);
		topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER); //NON-NLS-1

		//factory.addView("kr.openrobot.simulator.ui.views.DevicePropertyView", IPageLayout.RIGHT, 0.25f, factory.getEditorArea());
		factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
		factory.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
	}

	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); //NON-NLS-1
		factory.addActionSet("de.innot.avreclipse.actionset.avrdude");
		// Robot Simulator Action Set
		factory.addActionSet("kr.openrobot.simulator.ui.actions.SimulatorRun");
		factory.addActionSet("kr.openrobot.simulator.ui.actions.CodeGen");
		factory.addActionSet("kr.openrobot.simulator.ui.buildAction");
		factory.addActionSet("kr.openrobot.simulator.ui.DownAction");
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		//factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}