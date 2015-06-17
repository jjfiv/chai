package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.collections.iters.ClosingIterator;
import ciir.jfoley.chai.io.CompressionCodec;
import ciir.jfoley.chai.io.IO;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

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
      return new Iterable<InputStreamable>() {
        @Override
        public Iterator<InputStreamable> iterator() {
          try {
            return new TarIterator(new TarArchiveInputStream(IO.openInputStream(input)));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }

    @Override
    public void close() throws IOException {
    }
  }

  static class TarIterator implements ClosingIterator<InputStreamable> {
    public TarArchiveInputStream is;
    private TarArchiveEntry current;
    private TarArchiveEntry NOT_SURE = (TarArchiveEntry) new Object();

    TarIterator(TarArchiveInputStream is) throws IOException {
      this.is = is;
      consumeHeader();
    }

    private void consumeHeader() {
      try {
        current = is.getNextTarEntry();
        if(current == null) close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public boolean hasNext() {
      if(current == NOT_SURE) {
        consumeHeader();
      }
      return current != null;
    }

    @Override
    public InputStreamable next() {
      if(current == NOT_SURE) consumeHeader();

      final TarArchiveEntry prev = current;
      current = NOT_SURE;
      return new InputStreamable() {
        @Override
        public String getName() {
          return prev.getName();
        }

        @Override
        public InputStream getInputStream() throws IOException {
          assert(current == NOT_SURE);
          return CompressionCodec.wrapInputStream(prev.getName(), is);
        }
      };
    }

    @Override
    public void close() throws IOException {
      IO.close(is);
      is = null;
      current = null;
    }
  }
}
