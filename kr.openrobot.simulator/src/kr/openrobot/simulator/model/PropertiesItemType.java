package kr.openrobot.simulator.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import kr.openrobot.simulator.FavoritesLog;
//import com.qualityeclipse.favorites.FavoritesLog;

/**
 * The <code>FavoriteItemType</code> object is returned by the
 * {@link IFavoriteItem#getType()} method and represents a type-safe
 * enumeration that can be used for sorting and storing Favorites
 * objects. It has a human-readable name associated with it for
 * display purposes. Introducing the FavoriteItemType rather than a
 * simple String or int allows the sort order to be separated from the
 * human-readable name associated with a type of Favorites object.
 */
public abstract class PropertiesItemType
      implements Comparable<PropertiesItemType>
{
   private static final ISharedImages PLATFORM_IMAGES = 
      PlatformUI.getWorkbench().getSharedImages();

   private final String id;
   private final String printName;
   private final int ordinal;
   
   private PropertiesItemType(String id, String name, int position) {
      this.id = id;
      this.ordinal = position;
      this.printName = name;
   }
   
   public String getId() {
      return id;
   }
   
   public String getName() {
      return printName;
   }
   
   public abstract Image getImage();
   public abstract IPropertiesItem newProperties(Object obj);
   public abstract IPropertiesItem loadProperties(String info);
   
   public int compareTo(PropertiesItemType other) {
      return this.ordinal - other.ordinal;
   }

   ////////////////////////////////////////////////////////////////////////////
   //
   // Constants representing types of favorites objects.
   //
   ////////////////////////////////////////////////////////////////////////////

   public static final PropertiesItemType UNKNOWN 
      = new PropertiesItemType("Unknown", "Unknown", 0) 
   {
      public Image getImage() {
         return null;
      }
   
      public IPropertiesItem newProperties(Object obj) {
         return null;
      }
   
      public IPropertiesItem loadProperties(String info) {
         return null;
      }
   };
   
   public static final PropertiesItemType WORKBENCH_FILE 
      = new PropertiesItemType("WBFile", "Workbench File", 1) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
      }
   
      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IFile))
            return null;
         return new PropertiesResource(this, (IFile) obj);
      }
   
      public IPropertiesItem loadProperties(String info) {
         return PropertiesResource.loadFavorite(this, info);
      }
   };
   
   public static final PropertiesItemType WORKBENCH_FOLDER 
      = new PropertiesItemType("WBFolder", "Workbench Folder", 2)
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
      }
   
      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IFolder))
            return null;
         return new PropertiesResource(this, (IFolder) obj);
      }
   
      public IPropertiesItem loadProperties(String info) {
         return PropertiesResource.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType WORKBENCH_PROJECT 
      = new PropertiesItemType("WBProj", "WorkbenchProject", 3) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IProject))
            return null;
         return new PropertiesResource(this, (IProject) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesResource.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_PROJECT 
      = new PropertiesItemType("JProj", "Java Project", 4) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IJavaProject))
            return null;
         return new PropertiesJavaElement(this, (IJavaProject) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_PACKAGE_ROOT 
      = new PropertiesItemType("JPkgRoot", "Java Package Root", 5) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IPackageFragmentRoot))
            return null;
         return new PropertiesJavaElement(this,
               (IPackageFragmentRoot) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_PACKAGE 
      = new PropertiesItemType("JPkg", "Java Package", 6) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IPackageFragment))
            return null;
         return new PropertiesJavaElement(this, (IPackageFragment) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_CLASS_FILE 
      = new PropertiesItemType("JClass", "Java Class File", 7) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CFILE);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IClassFile))
            return null;
         return new PropertiesJavaElement(this, (IClassFile) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_COMP_UNIT 
      = new PropertiesItemType("JCompUnit", "Java Compilation Unit", 8) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CUNIT);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof ICompilationUnit))
            return null;
         return new PropertiesJavaElement(this, (ICompilationUnit) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_INTERFACE 
      = new PropertiesItemType("JInterface", "Java Interface", 9) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IType))
            return null;
         try {
            if (!((IType) obj).isInterface())
               return null;
         }
         catch (JavaModelException e) {
            FavoritesLog.logError(e);
         }
         return new PropertiesJavaElement(this, (IType) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   public static final PropertiesItemType JAVA_CLASS 
      = new PropertiesItemType("JClass", "Java Class", 10) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS);
      }

      public IPropertiesItem newProperties(Object obj) {
         if (!(obj instanceof IType))
            return null;
         try {
            if (((IType) obj).isInterface())
               return null;
         }
         catch (JavaModelException e) {
            FavoritesLog.logError(e);
         }
         return new PropertiesJavaElement(this, (IType) obj);
      }

      public IPropertiesItem loadProperties(String info) {
         return PropertiesJavaElement.loadFavorite(this, info);
      }
   };

   ////////////////////////////////////////////////////////////////////////////
   //
   // Accessors for all Favorite types
   //
   ////////////////////////////////////////////////////////////////////////////
   
   private static final PropertiesItemType[] TYPES = { UNKNOWN,
         WORKBENCH_FILE, WORKBENCH_FOLDER, WORKBENCH_PROJECT,
         JAVA_PROJECT, JAVA_PACKAGE_ROOT, JAVA_PACKAGE,
         JAVA_CLASS_FILE, JAVA_COMP_UNIT, JAVA_INTERFACE, JAVA_CLASS, };
   
   public static PropertiesItemType[] getTypes() {
      return TYPES;
   }
}
