package kr.openrobot.simulator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import kr.openrobot.simulator.Activator;
import kr.openrobot.simulator.FavoritesLog;
import kr.openrobot.simulator.core.project.ProjectManagement;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

public class PropertiesManager
{
	private static final String TAG_FAVORITES = "SingleJointRobotArmDeviceInfo";
	private static final String TAG_FAVORITE = "Device";
	private static final String TAG_TYPEID = "type";
	private static final String TAG_INFO = "DeviceName";
	private static final String TAG_PORT = "DevicePort";
	private static final String TAG_PWM = "PwmEnable";
	private static final String TAG_TIMER = "TimerType";
	private static final String TAG_MOVEMENTS = "Movements";
	

	private static PropertiesManager manager;
	private Collection<IPropertiesItem> properties;
	private List<PropertiesManagerListener> listeners =
			new ArrayList<PropertiesManagerListener>();

	//public static XPath xpath = XPathFactory.newInstance().newXPath();

	private PropertiesManager() {
	}
	/*
	 * Dummy
	 */

	ProjectManagement prjManage;
	// /////////////////////////////////////////////////////////////////////////
	//
	// Singleton
	//
	// /////////////////////////////////////////////////////////////////////////

	public static PropertiesManager getManager() {
		if (manager == null)
			manager = new PropertiesManager();
		return manager;
	}

	public IPropertiesItem[] getProperties() {
		if (properties == null)
			loadProperties();
		return properties.toArray(new IPropertiesItem[properties.size()]);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Accessing Favorite Items
	//
	// /////////////////////////////////////////////////////////////////////////

	public void addProperties(Object[] objects) {
		if (objects == null)
			return;
		if (properties == null)
			loadProperties();
		Collection<IPropertiesItem> items =
				new HashSet<IPropertiesItem>(objects.length);
		for (int i = 0; i < objects.length; i++) {
			IPropertiesItem item = existingPropertiesFor(objects[i]);
			if (item == null) {
				item = newPropertiesFor(objects[i]);
				if (properties.add(item))
					items.add(item);
			}
		}
		if (items.size() > 0) {
			IPropertiesItem[] added =
					items.toArray(new IPropertiesItem[items.size()]);
			firePropertiesChanged(added, IPropertiesItem.NONE);
		}
	}

	public void removeProperties(Object[] objects) {
		if (objects == null)
			return;
		if (properties == null)
			loadProperties();
		Collection<IPropertiesItem> items =
				new HashSet<IPropertiesItem>(objects.length);
		for (int i = 0; i < objects.length; i++) {
			IPropertiesItem item = existingPropertiesFor(objects[i]);
			if (item != null && properties.remove(item))
				items.add(item);
		}
		if (items.size() > 0) {
			IPropertiesItem[] removed =
					items.toArray(new IPropertiesItem[items.size()]);
			firePropertiesChanged(IPropertiesItem.NONE, removed);
		}
	}

	public IPropertiesItem newPropertiesFor(Object obj) {
		PropertiesItemType[] types = PropertiesItemType.getTypes();
		for (int i = 0; i < types.length; i++) {
			IPropertiesItem item = types[i].newProperties(obj);
			if (item != null)
				return item;
		}
		return null;
	}

	private IPropertiesItem existingPropertiesFor(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof IPropertiesItem)
			return (IPropertiesItem) obj;
		Iterator<IPropertiesItem> iter = properties.iterator();
		while (iter.hasNext()) {
			IPropertiesItem item = iter.next();
			if (item.isPropertiesFor(obj))
				return item;
		}
		return null;
	}

	public IPropertiesItem[] existingFavoritesFor(Iterator<?> iter) {
		List<IPropertiesItem> result = new ArrayList<IPropertiesItem>(10);
		while (iter.hasNext()) {
			IPropertiesItem item = existingPropertiesFor(iter.next());
			if (item != null)
				result.add(item);
		}
		return (IPropertiesItem[]) result.toArray(new IPropertiesItem[result.size()]);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Event Handling
	//
	// /////////////////////////////////////////////////////////////////////////

	public void addPropertiesManagerListener(
			PropertiesManagerListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removePropertiesManagerListener(
			PropertiesManagerListener listener) {
		listeners.remove(listener);
	}

	private void firePropertiesChanged(IPropertiesItem[] itemsAdded,
			IPropertiesItem[] itemsRemoved) {
		PropertiesManagerEvent event =
				new PropertiesManagerEvent(this, itemsAdded, itemsRemoved);
		for (Iterator<PropertiesManagerListener> iter =
				listeners.iterator(); iter.hasNext();)
			((PropertiesManagerListener) iter.next()).PropertiesChanged(event);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Persisting favorites
	//
	// /////////////////////////////////////////////////////////////////////////

	private void loadProperties() {
		properties = new HashSet<IPropertiesItem>(20);
		FileReader reader = null;
		try {
			reader = new FileReader(getPropertiesFile());
			loadProperties(XMLMemento.createReadRoot(reader));
		}
		catch (FileNotFoundException e) {
			// Ignored... no Favorites items exist yet.
		}
		catch (Exception e) {
			// Log the exception and move on.
			FavoritesLog.logError(e);
		}
		finally {
			try {
				if (reader != null)
					reader.close();
			}
			catch (IOException e) {
				FavoritesLog.logError(e);
			}
		}
	}

	private void loadProperties(XMLMemento memento) {
		IMemento[] children = memento.getChildren(TAG_FAVORITE);
		for (int i = 0; i < children.length; i++) {
			IPropertiesItem item =
					newPropertiesFor(children[i].getString(TAG_TYPEID),
							children[i].getString(TAG_INFO));
			if (item != null)
				properties.add(item);
		}
	}

	public IPropertiesItem newPropertiesFor(String typeId, String info) {
		PropertiesItemType[] types = PropertiesItemType.getTypes();
		for (int i = 0; i < types.length; i++)
			if (types[i].getId().equals(typeId))
				return types[i].loadProperties(info);
		return null;
	}



	public void saveProperties() {
		if (properties == null)
			return;
		XMLMemento memento = XMLMemento.createWriteRoot(TAG_FAVORITES);
		saveProperties(memento);
		FileWriter writer = null;
		try {
			//writer = new FileWriter(getPropertiesFile());
			writer = new FileWriter("/home/yeongchann/DeviceInfo.xml");
			memento.save(writer);
		}
		catch (IOException e) {
			FavoritesLog.logError(e);
		}
		finally {
			try {
				if (writer != null)
					writer.close();
			}
			catch (IOException e) {
				FavoritesLog.logError(e);
			}
		}
	}

	private void saveProperties(XMLMemento memento) {
		Iterator<IPropertiesItem> iter = properties.iterator();
		while (iter.hasNext()) {
			IPropertiesItem item = iter.next();
			IMemento child = memento.createChild(TAG_FAVORITE);
			child.putString(TAG_TYPEID, item.getType().getId());
			child.putString(TAG_INFO, item.getInfo());
			child.putString(TAG_PORT, item.getInfo());
			child.putString(TAG_PWM, item.getInfo());
			child.putString(TAG_TIMER, item.getInfo());
			child.putString(TAG_MOVEMENTS, item.getInfo());
		}
	}

	private File getPropertiesFile() {
		return Activator.getDefault()
				.getStateLocation()
				.append("DeviceInfo.xml")
				.toFile();
	}
}
