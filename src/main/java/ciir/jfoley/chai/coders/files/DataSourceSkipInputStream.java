package ciir.jfoley.chai.coders.files;

import ciir.jfoley.chai.io.streams.CachedSkipInputStream;
import ciir.jfoley.chai.io.streams.SkipInputStream;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public class DataSourceSkipInputStream extends SkipInputStream {
  final DataSource mySource;

  long position = 0;
  long size = 0;

  public DataSourceSkipInputStream(DataSource mySource) throws IOException {
    this.mySource = mySource;
    this.position = 0;
    this.size = mySource.size();
  }

  @Override
  public int read() throws IOException {
    return mySource.read(position++);
  }

  public long remaining() {
    return size - position;
  }

  @Override
  public int read(@Nonnull byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  @Override
  public int read(@Nonnull byte[] b, int off, int len) throws IOException {
    int amount = Math.min(len - off, (int) remaining());
    ByteBuffer buf = mySource.read(position, amount);
    buf.rewind();
    assert(buf.position() == 0);
    buf.get(b, off, Math.min(buf.remaining(), len));

    int read = buf.position();
    // increase read pointer
    position += read;
    // return amount read:
    return read;
  }

  @Override
  public long tell() throws IOException {
    return position;
  }

  @Override
  public void seekRelative(long delta) throws IOException {
    position += delta;
  }

  @Override
  public void seek(long offset) throws IOException {
    position = offset;
  }

  @Override
  public void close() throws IOException {
    // actually nothing to do here.
  }

  public DataSource sourceAtCurrentPosition() throws IOException {
    return mySource.view(this.position);
  }

  public CachedSkipInputStream cached() {
    return new CachedSkipInputStream(this);
  }
}
