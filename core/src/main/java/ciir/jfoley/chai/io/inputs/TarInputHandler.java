package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.collections.iters.ClosingIterator;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.StreamFns;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public class TarInputHandler implements InputFinder.FileHandler {
  @Override
  public boolean matches(File input) {
    String name = input.getName();
    return name.endsWith(".tar.gz") || name.endsWith(".tar") || name.endsWith(".tar.bz") || name.endsWith(".tar.bz2");
  }

  @Override
  public InputContainer getContainer(File input) throws IOException {
    return new TarInputContainer(input);
  }

  static class TarInputContainer implements InputContainer {
    File input;

    public TarInputContainer(File fp) {
      this.input = fp;
    }

    @Override
    public Iterable<? extends InputStreamable> getInputs() {
      return () -> {
        try {
          return new TarIterator(new TarArchiveInputStream(IO.openInputStream(input)));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      };
    }

    @Override
    public String getName() {
      return input.getName();
    }

    @Override
    public void close() throws IOException {
    }
  }

  /**
   * When you call hasNext() or next() you invalidate the previous InputStreamable.
   */
  static class TarIterator implements ClosingIterator<InputStreamable> {
    public TarArchiveInputStream is;
    private TarArchiveEntry current;
    private boolean needsConsume = true;

    TarIterator(TarArchiveInputStream is) throws IOException {
      this.is = is;
      consumeHeader();
    }

    private void consumeHeader() {
      try {
        current = is.getNextTarEntry();
        needsConsume = false;
        if(current == null) close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public boolean hasNext() {
      if(needsConsume) consumeHeader();
      return current != null;
    }

    @Override
    public InputStreamable next() {
      if(needsConsume) consumeHeader();

      final TarArchiveEntry prev = current;
      needsConsume = true; // need to pull next lazily so that they can read the adjacent stream:
      return new InputStreamable() {
        @Override
        public String getName() {
          return prev.getName();
        }

        @Override
        public InputStream getRawInputStream() throws IOException {
          assert(needsConsume);
          // If they want it, slurp it to memory so that we they can't close the underlying Tar input stream.
          return new ByteArrayInputStream(StreamFns.readBytes(is, IntMath.fromLong(prev.getSize())));
        }
      };
    }

    @Override
    public void close() throws IOException {
      IO.close(is);
      needsConsume = false;
      is = null;
      current = null;
    }
  }
}
