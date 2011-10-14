/**
 * @author Andrés Villalobos and Santiago Sanchez
 */
package acsucn.eclipse.plugin.core.projectstructure;

import java.io.*;
import java.util.LinkedList;

import org.eclipse.core.runtime.Platform;

public class ACSProjectStructure {

	private OperativeSystem OS;
	private int MaxCantFolders;
	private String ProjectName;
	private ACSTemplateFiles template;

	/**
	 * Constructor
	 * @param _OS Indicate the Operative System where the plugins is working
	 * @param projectName Indicate the project name of the new project that will be created
	 * @author Andrés Villalobos and Santiago Sanchez
	 */
	public ACSProjectStructure(OperativeSystem _OS, String projectName) {
		this.OS = _OS;
		this.MaxCantFolders = 29;
		this.ProjectName = projectName;
		template = new ACSTemplateFiles(_OS,projectName);
	}

	/**
	 * This method create a complete directory for a ACS project as
	 * getTemplateForDirectory script by without files
	 * 
	 * @return A list of FAILED directories, if all directories were created,
	 *         then return null.
	 * @author Andrés Villalobos and Santiago Sanchez
	 */
	public LinkedList<String> createProjectStructure() {
		// String WorkSpace = System.getProperty("user.dir");
		// Note that this will work only if is inside of a plugins, for standart
		// test, you can't use it
		// TODO Check if there is another way of get the Workspace
		String WorkSpace = Platform.getLocation().toFile().getAbsolutePath();

		String FullPathProjectDirectory = WorkSpace	+ this.OS.getDirectorySeparator() + this.ProjectName;
		LinkedList<String> foldersPath = getAcsProjectFolders(FullPathProjectDirectory);
		LinkedList<String> foldersFailed = new LinkedList<String>();
		int i = 0;
		for (String folder : foldersPath) {
			System.out.println("[" + i + "]" + folder);
			if (!createDirectory(folder)) {
				System.out.println("[" + i + "]" + folder + "   Error!!");
				foldersFailed.add(folder);
			}
			i++;
		}
		template.CreateFiles();
		if (foldersFailed.size() != 0) {
			System.out.println("The following directorys was unable to be created");
			for (String folder : foldersFailed) {
				System.out.println(folder);
			}
			return foldersFailed;
		} else {
			return null;
		}
	}

	/**
	 * This method create a directory with an specific path
	 * 
	 * @param path
	 *            The fully path for create a folder
	 * @author Andrés Villalobos and Santiago Sanchez
	 */
	private boolean createDirectory(String path) {
		try {
			File Directorio = new File(path);

			// El Directorio raiz no existe Verifica si el directorio creado
			// existe
			if (Directorio.mkdirs()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			System.out.print("Se ha producido un error" + ex);
			return false;
		}
	}

	/**
	 * This method return a list of fixed paths for the choosen Operative system.
	 * 
	 * @return A linked list with full paths to create the folders
	 * @author Andrés Villalobos
	 */
	private LinkedList<String> getAcsProjectFolders(String FullPathProjectDirectory) {
		LinkedList<String> folders = new LinkedList<String>();
		//folders.add(FullPathProjectDirectory);
		for (int i = 1; i <= this.MaxCantFolders; i++) {
			folders.add(FullPathProjectDirectory + getDirectoryName(i));
		}
		return folders;
	}

	/**
	 * Method that return the fixed name of the folder and sub folder for ACS Structure
	 * 
	 * @param dir Indicate the index of the dir that is needed
	 * @author Andrés Villalobos and Santiago Sanchez
	 */
	private String getDirectoryName(int dir) {
		String ReturnDirectory = "";
		switch (dir) {
		case 1:
			return this.OS.getDirectorySeparator() + "bin";
		case 2:
			return this.OS.getDirectorySeparator() + "include";
		case 3:
			return this.OS.getDirectorySeparator() + "idl";
		case 4:
			return this.OS.getDirectorySeparator() + "lib";
		case 5:
			return this.OS.getDirectorySeparator() + "lib"
					+ this.OS.getDirectorySeparator() + "endorsed";
		case 6:
			return this.OS.getDirectorySeparator() + "lib"
					+ this.OS.getDirectorySeparator() + "python";
		case 7:
			return this.OS.getDirectorySeparator() + "lib"
					+ this.OS.getDirectorySeparator() + "ACScomponents";
		case 8:
			return this.OS.getDirectorySeparator() + "lib"
					+ this.OS.getDirectorySeparator() + "python"
					+ this.OS.getDirectorySeparator() + "site-packages";
		case 9:
			return this.OS.getDirectorySeparator() + "man";
		case 10:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man1";
		case 11:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man2";
		case 12:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man3";
		case 13:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man4";
		case 14:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man5";
		case 15:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man6";
		case 16:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man7";
		case 17:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "man8";
		case 18:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "mann";
		case 19:
			return this.OS.getDirectorySeparator() + "man"
					+ this.OS.getDirectorySeparator() + "manl";
		case 20:
			return this.OS.getDirectorySeparator() + "doc";
		case 21:
			return this.OS.getDirectorySeparator() + "object";
		case 22:
			return this.OS.getDirectorySeparator() + "LOGS";
		case 23:
			return this.OS.getDirectorySeparator() + "test";
		case 24:
			return this.OS.getDirectorySeparator() + "src";
		case 25:
			return this.OS.getDirectorySeparator() + "rtai";
		case 26:
			return this.OS.getDirectorySeparator() + "config";
		case 27:
			return this.OS.getDirectorySeparator() + "config"
					+ this.OS.getDirectorySeparator() + "CDB";
		case 28:
			return this.OS.getDirectorySeparator() + "config"
					+ this.OS.getDirectorySeparator() + "CDB"
					+ this.OS.getDirectorySeparator() + "schemas";
		case 29:
			return this.OS.getDirectorySeparator() + "changeLog";
		}
		return ReturnDirectory;
	}
}
