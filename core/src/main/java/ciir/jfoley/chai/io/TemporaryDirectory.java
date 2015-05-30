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
public class TemporaryDirectory extends Directory implements Closeable, GenerateFn<File> {
  private final static Logger logger = Logger.getLogger(TemporaryDirectory.class.getName());
  private final static FileAttribute[] None = new FileAttribute[0];


  public TemporaryDirectory(String prefix) throws IOException {
    super(Files.createTempDirectory(prefix, None).toFile());
  }

  public TemporaryDirectory() throws IOException {
    this("chaitmpdir");
  }

  /**
   * The idea here is to automagically delete all temporary contents when this close() gets called.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    List<File> stubborn = this.removeRecursively();
    if(!stubborn.isEmpty()) {
      throw new IOException("Leaked " + stubborn.size() + " files because of TemporaryDirectory: " + dir.getAbsolutePath()+" ... Double check your permissions!");
    }
  }
}
