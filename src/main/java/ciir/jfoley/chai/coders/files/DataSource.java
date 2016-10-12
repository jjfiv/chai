package ciir.jfoley.chai.coders.files;

import ciir.jfoley.chai.coders.kinds.FixedSize;
import ciir.jfoley.chai.io.streams.CachedSkipInputStream;
import ciir.jfoley.chai.io.streams.SkipInputStream;
import ciir.jfoley.chai.io.streams.StaticStream;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author jfoley.
 */
public interface DataSource extends Closeable {
  long size() throws IOException;
  ByteBuffer read(long position, int size) throws IOException;
  DataSource view(long position, long size) throws IOException;
  default DataSource view(long position) throws IOException {
    return view(position, size() - position);
  }

  /**
   * Read a long at the given position in this source.
   * @param position the offset to read from.
   * @return the long value.
   * @throws IOException on any sort of problems.
   */
  default long readLong(long position) throws IOException {
    return FixedSize.longs.read(read(position, 8));
  }

  default int readInt(long position) throws IOException {
    return FixedSize.ints.read(read(position, 4));
  }

  /**
   * Reads a byte from this source like its an InputStream:
   * {@link java.io.InputStream#read}
   * @param position the offset to read from
   * @return the byte or -1 if you're not in a valid position.
   * @throws IOException
   */
  int read(long position) throws IOException;

  default DataSourceSkipInputStream stream(long position) throws IOException {
    return view(position).stream();
  }

  default StaticStream getSource(long start, int size) {
    return new StaticStream() {
      @Override
      public SkipInputStream getNewStream() throws IOException {
        return new CachedSkipInputStream(new DataSourceSkipInputStream(view(start, size)));
        //return new DataSourceSkipInputStream(view(start, size));
      }

      @Override public long length() { return size; }
    };
  }

  default DataSourceSkipInputStream stream() throws IOException {
    return new DataSourceSkipInputStream(this);
  }
}
