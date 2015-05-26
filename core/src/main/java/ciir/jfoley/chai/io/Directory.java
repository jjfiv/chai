package ciir.jfoley.chai.io;

import ciir.jfoley.chai.fn.GenerateFn;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A wrapper around a File known to be a directory, for ease of use of FS utilities.
 * @author jfoley
 */
public class Directory implements GenerateFn<File> {
  protected final File dir;

  public Directory(String path) {
    this.dir = new File(path);
    if(!dir.mkdirs()) {
      throw new RuntimeException("Cannot obtain or create "+path+" as a directory.");
    }
  }

  public Directory(File input) {
    if(!input.mkdirs()) {
      throw new RuntimeException("Cannot obtain or create "+input.getAbsolutePath()+" as a directory.");
    }
    assert(input.isDirectory());
    dir = input;
  }

  public File child(String name) {
    return new File(dir, name);
  }
  public Directory childDir(String name) {
    return new Directory(new File(dir, name));
  }
  public String childPath(String name) {
    return child(name).getAbsolutePath();
  }

  public List<File> children() throws IOException {
    return FS.listDirectory(dir);
  }

  public List<File> recursiveChildren() throws IOException {
    return FS.findFilesRecursively(dir);
  }

  public List<File> removeRecursively() throws IOException {
    return FS.removeDirectoryRecursively(dir);
  }

  @Override
  public File get() {
    return dir;
  }
}
