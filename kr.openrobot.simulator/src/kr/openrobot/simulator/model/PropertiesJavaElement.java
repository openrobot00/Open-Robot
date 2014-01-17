package kr.openrobot.simulator.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import kr.openrobot.simulator.FavoritesLog;

//import com.qualityeclipse.favorites.FavoritesLog;

public class PropertiesJavaElement
		implements IPropertiesItem
{
	private PropertiesItemType type;
	private IJavaElement element;
	private String name;

	public PropertiesJavaElement(PropertiesItemType type, IJavaElement element) {
		this.type = type;
		this.element = element;
	}

	public static PropertiesJavaElement loadFavorite(PropertiesItemType type,
			String info) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				new Path(info));
		if (res == null)
			return null;
		IJavaElement elem = JavaCore.create(res);
		if (elem == null)
			return null;
		return new PropertiesJavaElement(type, elem);
	}

	public String getName() {
		if (name == null)
			name = element.getElementName();
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getLocation() {
		try {
			IResource res = element.getUnderlyingResource();
			if (res != null) {
				IPath path = res.getLocation().removeLastSegments(1);
				if (path.segmentCount() == 0)
					return "";
				return path.toString();
			}
		} catch (JavaModelException e) {
			FavoritesLog.logError(e);
		}
		return "";
	}

	public boolean isPropertiesFor(Object obj) {
		return element.equals(obj);
	}

	public PropertiesItemType getType() {
		return type;
	}

	public boolean equals(Object obj) {
		return this == obj
				|| ((obj instanceof PropertiesJavaElement) && element
						.equals(((PropertiesJavaElement) obj).element));
	}

	public int hashCode() {
		return element.hashCode();
	}

	// For now, this is how we suppress a warning that we cannot fix
	// See Bugzilla #163093 and Bugzilla #149805 comment #14
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return getAdapterDelegate(adapter);
	}

	private Object getAdapterDelegate(Class<?> adapter) {
		if (adapter.isInstance(element))
			return element;
		IResource resource = element.getResource();
		if (adapter.isInstance(resource))
			return resource;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public String getInfo() {
		try {
			return element.getUnderlyingResource().getFullPath().toString();
		} catch (JavaModelException e) {
			FavoritesLog.logError(e);
			return null;
		}
	}

//	@Override
//	public boolean isPropertiesFor(Object obj) {
//		// TODO Auto-generated method stub
//		return false;
//	}
}