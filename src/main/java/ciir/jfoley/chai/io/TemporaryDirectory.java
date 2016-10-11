package ciir.jfoley.chai.io;

import ciir.jfoley.chai.fn.GenerateFn;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create and delete temporary directories as resources.
 * @author jfoley.
 */
public class TemporaryDirectory extends Directory implements Closeable, GenerateFn<File> {
  private final static Logger logger = Logger.getLogger(TemporaryDirectory.class.getName());
  private final static FileAttribute[] None = new FileAttribute[0];
  private boolean closed = false;

  private AtomicLong uid = new AtomicLong(1);

  public TemporaryDirectory(String prefix) throws IOException {
    super(Files.createTempDirectory(prefix, None).toFile());
  }

  public TemporaryDirectory() throws IOException {
    this("chaitmpdir");
  }

  @Override
  public void finalize() throws Throwable {
    super.finalize();
    if(!closed) logger.log(Level.SEVERE, "Leaked TemporaryFile!");
    assert(closed);
  }

  public File newOrderedFile(String ext) {
    return this.child(uid.getAndIncrement()+ext);
  }

  /**
   * The idea here is to automagically delete all temporary contents when this close() gets called.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    closed = true;
    List<File> stubborn = this.removeRecursively();
    if(!stubborn.isEmpty()) {
      throw new IOException("Leaked " + stubborn.size() + " files because of TemporaryDirectory: " + dir.getAbsolutePath()+" ... Double check your permissions!");
    }
  }
}
