package ciir.jfoley.chai.coders.files;

import ciir.jfoley.chai.coders.kinds.FixedSize;

import java.io.IOException;

/**
 * @author jfoley
 */
public class WriteLongLater {
  public final DataSink file;
  public long offset;

  public WriteLongLater(DataSink file) throws IOException {
    this.file = file;
    this.offset = file.tell();
    file.write(FixedSize.longs, 0L);
  }

  public void write(long value) throws IOException {
    file.write(offset, FixedSize.longs, value);
  }
}
