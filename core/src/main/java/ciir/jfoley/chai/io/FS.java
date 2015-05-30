package ciir.jfoley.chai.io;

import ciir.jfoley.chai.lang.Module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author jfoley.
 */
public class FS extends Module {
  public static final Logger logger = Logger.getLogger(FS.class.getName());
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
      throw new IOException("Couldn't list the directory -- got null! "+dir);
    }

    return Arrays.asList(data);
  }

  public static List<File> findFilesRecursively(File base) throws IOException {
    List<File> foundFiles = new ArrayList<>();
    Queue<File> directories = new LinkedList<>();
    directories.add(base);

    while(!directories.isEmpty()) {
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

  /**
   * Instead of throwing errors, logs and returns a list of un-delete-able items.
   * @param base the base folder to delete all of.
   * @throws IOException
   */
  public static List<File> removeDirectoryRecursively(File base) throws IOException {
    List<File> stubborn = new ArrayList<>();

    for (File child : FS.listDirectory(base)) {
      if(child.isDirectory()) {
        stubborn.addAll(removeDirectoryRecursively(child));
      } else {
        if(!child.delete()) {
          logger.warning("Couldn't delete file: "+child.getAbsolutePath());
          stubborn.add(child);
        }
      }
    }
    if(!base.delete()) {
      logger.warning("Couldn't delete directory: "+base.getAbsolutePath());
      stubborn.add(base);
    }
    return stubborn;
  }

  public static boolean isDirectory(String indexPath) {
    return new File(indexPath).isDirectory();
  }
}
