package ciir.jfoley.chai.io;

import ciir.jfoley.chai.lang.Module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author jfoley.
 */
public class FS extends Module {
	// Module.
	private FS() {  }

	public static List<File> listDirectory(String path) throws IOException {
		return listDirectory(new File(path));
	}

	public static List<File> listDirectory(File dir) throws IOException {
		if(!dir.exists()) {
			throw new FileNotFoundException("No directory to list! "+dir);
		}
		if(!dir.canRead()) {
			throw new IOException("Can't read directory! Permissions? "+dir);
		}
		if(!dir.isDirectory()) {
			throw new IOException("Isn't a directory: "+dir);
		}

		File[] data = dir.listFiles();
		if(data == null) {
			throw new IOException("Couldn't list the directory -- got all nulls! "+dir);
		}

		return Arrays.asList(data);
	}

	public static List<File> findFilesRecursively(File base) throws IOException {
		List<File> foundFiles = new ArrayList<>();
		Queue<File> directories = new LinkedList<>();
		directories.add(base);

		while(!directories.isEmpty()) {
			System.out.println(directories);
			File possible = directories.poll();
			assert(possible != null);
			for (File file : listDirectory(possible)) {
				if(file.isDirectory()) {
					directories.offer(file);
				} else {
					foundFiles.add(file);
				}
			}
		}

		return foundFiles;
	}


}
