package ciir.jfoley.chai.io;

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
public class TemporaryDirectory implements Closeable {
  private final static Logger logger = Logger.getLogger(TemporaryDirectory.class.getName());
  private final static FileAttribute[] None = new FileAttribute[0];
  private final File directory;


  public TemporaryDirectory(String prefix) throws IOException {
    this.directory = Files.createTempDirectory(prefix, None).toFile();
  }

  public TemporaryDirectory() throws IOException {
    this("chaitmpdir");
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

  @Override
  public void close() throws IOException {
    for (File file : recursiveChildren()) {
      if(!file.delete()) {
        logger.warning("Couldn't delete temporary file inside directory: "+file.getAbsolutePath());
      }
    }
    if(!directory.delete()) {
      logger.warning("Couldn't delete temporary directory: " + directory.getAbsolutePath());
    }

  }

}
