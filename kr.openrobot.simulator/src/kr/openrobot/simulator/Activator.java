package kr.openrobot.simulator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "kr.openrobot.simulator"; //$NON-NLS-1$
	public static final String	DEFAULT_CONSOLE	= "AVR Eclipse Plugin Log";


	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}


	
	public MessageConsole getDefaultConsole() {
		return getConsole(DEFAULT_CONSOLE);
	}
	
	public MessageConsole getConsole(String name) {
			
			// Check if we are running headless (JUnit Test e.g.)
			// If Headless we return null
			Bundle b = Platform.getBundle("org.eclipse.ui"); 
			if ((b==null) || (b.getState() != Bundle.ACTIVE) || (! PlatformUI.isWorkbenchRunning())) {
				return null;
			} 
			
			// Get a list of all known Consoles from the Global Console Manager and
			// see if a Console with the given name already exists.
			IConsoleManager conman = ConsolePlugin.getDefault().getConsoleManager();
			IConsole[] allconsoles = conman.getConsoles();
			for (IConsole console : allconsoles) {
				if (console.getName().equals(name)) {
					return (MessageConsole) console;
				}
			}
			// Console not found - create a new one
			MessageConsole newconsole = new MessageConsole(name, null);
			conman.addConsoles(new IConsole[] { newconsole });
			return newconsole;
	
	}
	
}
