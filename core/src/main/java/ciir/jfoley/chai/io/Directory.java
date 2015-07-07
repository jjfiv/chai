package ciir.jfoley.chai.io;

import ciir.jfoley.chai.fn.GenerateFn;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

/**
 * A wrapper around a File known to be a directory, for ease of use of FS utilities.
 * @author jfoley
 */
public class Directory implements GenerateFn<File> {
  protected final File dir;

  public Directory(String path) {
    this.dir = new File(path);
    ensure(dir);
  }

  public Directory(File input) {
    dir = input;
    ensure(dir);
  }

  /**
   * Ensures that the given input is a directory or creates it.
   * @param input the file or path as input.
   */
  public static void ensure(File input) {
    if(input.isDirectory()) return;
    if(!input.exists()) {
      if(!input.mkdirs()) {
        throw new RuntimeException("Cannot obtain or create "+input.getAbsolutePath()+" as a directory.");
      }
    }
    if(input.exists() && !input.isDirectory()) {
      throw new RuntimeException(input+" already exists and is NOT a directory.");
    }
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
  public String toString() {
    return "directory "+dir.toString();
  }

  @Override
  public File get() {
    return dir;
  }

  public String getPath() {
    return dir.getAbsolutePath();
  }

  public void ls(PrintStream output) throws IOException {
    for (File file : children()) {
      long size = Files.size(file.toPath());
      output.printf("%-40s\t%d\n", file.getName(), size);
    }
  }

  /**
   * Create a directory of an already-existing path! This will crash if ofPath does not exist as a directory.
   * @param ofPath the path.
   * @return Directory
   * @throws IOException if the directory does not exist.
   */
  public static Directory Read(String ofPath) throws IOException {
    File fp = new File(ofPath);
    if(!fp.exists()) throw new IOException("Directory: "+ofPath+" does not exist!");
    if(!fp.isDirectory()) throw new IOException(ofPath+" is required to be a directory!");
    return new Directory(fp);
  }

  @Nullable
  public static Directory FirstExisting(String... paths) {
    for (String path : paths) {
      File fp = new File(path);
      if(!fp.exists()) continue;
      if(!fp.isDirectory()) continue;
      return new Directory(fp);
    }
    return null;
  }

  public boolean isEmpty() throws IOException {
    return children().isEmpty();
  }
}
