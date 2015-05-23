package ciir.jfoley.chai.io;

import ciir.jfoley.chai.fn.GenerateFn;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.logging.Logger;

/**
 * Create and delete temporary directories as resources.
 * @author jfoley.
 */
public class TemporaryDirectory implements Closeable, GenerateFn<File> {
  private final static Logger logger = Logger.getLogger(TemporaryDirectory.class.getName());
  private final static FileAttribute[] None = new FileAttribute[0];
  private final File directory;


  public TemporaryDirectory(String prefix) throws IOException {
    this.directory = Files.createTempDirectory(prefix, None).toFile();
  }

  public TemporaryDirectory() throws IOException {
    this("chaitmpdir");
  }

  @Override
  public File get() {
    return directory;
  }

  public File child(String name) {
    return new File(directory, name);
  }
  public String childPath(String name) {
    return child(name).getAbsolutePath();
  }

  public List<File> children() throws IOException {
    return FS.listDirectory(directory);
  }

  public List<File> recursiveChildren() throws IOException {
    return FS.findFilesRecursively(directory);
  }

  /**
   * The idea here is to automagically delete all temporary contents when this close() gets called. Sadly, it's not implemented correctly now.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    List<File> stubborn = FS.removeDirectoryRecursively(this.directory);
    if(!stubborn.isEmpty()) {
      throw new IOException("Leaked " + stubborn.size() + " files because of TemporaryDirectory: " + directory.getAbsolutePath()+" ... Double check your permissions!");
    }
  }

}
