package kr.openrobot.simulator.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class PropertiesResource
		implements IPropertiesItem
{
	private PropertiesItemType type;
	private IResource resource;
	private String name;

	PropertiesResource(PropertiesItemType type, IResource resource) {
		this.type = type;
		this.resource = resource;
	}

	public static PropertiesResource loadFavorite(PropertiesItemType type,
			String info) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				new Path(info));
		if (res == null)
			return null;
		return new PropertiesResource(type, res);
	}

	public String getName() {
		if (name == null)
			name = resource.getName();
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getLocation() {
		IPath path = resource.getLocation().removeLastSegments(1);
		if (path.segmentCount() == 0)
			return "";
		return path.toString();
	}

	public boolean isFavoriteFor(Object obj) {
		return resource.equals(obj);
	}

	public PropertiesItemType getType() {
		return type;
	}

	public boolean equals(Object obj) {
		return this == obj
				|| ((obj instanceof PropertiesResource) && resource
						.equals(((PropertiesResource) obj).resource));
	}

	public int hashCode() {
		return resource.hashCode();
	}

	// For now, this is how we suppress a warning that we cannot fix
	// See Bugzilla #163093 and Bugzilla #149805 comment #14
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
      return getAdapterDelegate(adapter);
   }

	private Object getAdapterDelegate(Class<?> adapter) {
		if (adapter.isInstance(resource))
			return resource;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public String getInfo() {
		return resource.getFullPath().toString();
	}

	@Override
	public boolean isPropertiesFor(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}
}