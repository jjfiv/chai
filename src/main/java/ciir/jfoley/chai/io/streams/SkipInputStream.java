package ciir.jfoley.chai.io.streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public abstract class SkipInputStream extends InputStream {
  @Override
  public abstract int read() throws IOException;

  /** Returns the current offset into the "inputStream" */
  public abstract long tell() throws IOException;

  /** Moves to the given offset into this input stream. */
  public void seek(long offset) throws IOException {
    seekRelative(offset - tell());
  }
  public abstract void seekRelative(long delta) throws IOException;

  /** Make subclasses implement close! */
  public abstract void close() throws IOException;

  public static SkipInputStream wrap(InputStream stream) {
    if(stream instanceof SkipInputStream) return (SkipInputStream) stream;
    return new NaiveSkipInputStream(stream);
  }
}
