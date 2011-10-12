package acsucn.eclipse.plugin.core.projectpreferences;

/**
 * @author avillalobos
 */
import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class ACSClasspathContainer {

	private ArrayList<String> extensionsToFilter;
	
	public ACSClasspathContainer(ArrayList<String> extensions){
		if(extensions == null){
			throw new NullPointerException("You can't use this plugins if you doesn't specify at least one extension file to filter");
		}else{
			this.extensionsToFilter = extensions;
			System.out.println("[ClasspathContainer] Constructor");
		}
	}
	
	/*
	private FilenameFilter dirFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			// dir could be anywhere, but i think that is better verify this
			// dir with the ACS variables that now exist
			String[] segments = name.split("[.]");
			// the library could have more than 1 point, so it is important that have at least one
			if(segments.length < 2){
				return false;
				// the last segment contains the extension of the archive
			}else if(extensionsToFilter.contains(segments[segments.length - 1].toLowerCase())){
				System.out.println("Extension found " + segments[segments.length - 1]);
				return true;
			}else{
				return false;
			}
		}
	};
	
	/*
	public IClasspathEntry[] getClasspathEntries(Path location) {
		System.out.println("[ACS] getClasspathEntries");
		// TODO Auto-generated method stub
		// Aqui es donde hay que revisar todo el directorio en busca de las librerías que se podrían usar
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
		// Agregar aqui el path obtenido por las otras clases
		File dir = new File(location.toOSString());
		File[] libs = dir.listFiles(dirFilter);
		for(File lib : libs){
			System.out.println(lib.getName());
			entryList.add(JavaCore.newLibraryEntry(new Path(lib.getAbsolutePath()), null, new Path("/")));
		}
		System.out.println("[ACS] end getClasspathEntries");
        // convert the list to an array and return it
        IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
        return (IClasspathEntry[])entryList.toArray(entryArray);
	}
	*/
	
	// src could be null and lib must to be the full path
	public IClasspathEntry getClasspathEntry(Path lib,Path src){
		try{
		if(this.extensionsToFilter.contains(lib.getFileExtension()) && new File(lib.toOSString()).exists()){
			System.out.println("Librería " + lib.toOSString() + " aceptada");
			return JavaCore.newLibraryEntry(lib, src, new Path("/"));
		}else{
			return null;
		}
		}catch(NullPointerException npe){
			return null;
		}
	}
	
	public IClasspathEntry[] updateClasspathEntries(IClasspathEntry[] oldClassPathEntries, IClasspathEntry[] newClassPathEntries){
		ArrayList<IClasspathEntry> List = new ArrayList<IClasspathEntry>();
		for(IClasspathEntry ice : oldClassPathEntries){
			List.add(ice);
		}
		
		for(IClasspathEntry ice : newClassPathEntries){
			List.add(ice);
		}
		IClasspathEntry[] result = new IClasspathEntry[List.size()];
		return (IClasspathEntry[])List.toArray(result);
	}
	
	public void addExtensionsToFilter(String extension){
		this.extensionsToFilter.add(extension);
	}
	
	public void addExntensionsToFilter(ArrayList<String> exntesions){
		this.extensionsToFilter.addAll(exntesions);
	}
}
