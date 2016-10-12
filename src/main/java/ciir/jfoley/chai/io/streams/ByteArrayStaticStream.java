package ciir.jfoley.chai.io.streams;

import java.io.ByteArrayInputStream;

/**
 * @author jfoley
 */
public class ByteArrayStaticStream implements StaticStream {
  private final byte[] data;

  public ByteArrayStaticStream(byte[] data) {
    this.data = data;
  }
  @Override
  public SkipInputStream getNewStream() {
    return new NaiveSkipInputStream(new ByteArrayInputStream(data));
  }

  @Override
  public long length() {
    return data.length;
  }
}
