package ciir.jfoley.chai.coders.files;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author jfoley.
 */
public class OffsetDataSource implements DataSource {
  public final DataSource parent;
  public final long offset;
  public final long size;

  OffsetDataSource(DataSource parent, long position, long size) {
    this.parent = parent;
    this.offset = position;
    this.size = size;
  }


  public static DataSource FromPositionSize(DataSource parent, long position, long size) {
    return new OffsetDataSource(parent, position, size);
  }

  @Override
  public long size() throws IOException {
    return size;
  }

  @Override
  public ByteBuffer read(long position, int size) throws IOException {
    return parent.read(position+offset, size);
  }

  @Override
  public DataSource view(long position, long size) throws IOException {
    if(position + size > size()) throw new IllegalArgumentException("Can't request that much!");

    return parent.view(position+offset, size);
  }

  @Override
  public int read(long position) throws IOException {
    return parent.read(position+offset);
  }

  @Override
  public void close() throws IOException {
    // Don't necessarily close the parent.
    // Don't think we want that.
  }
}
