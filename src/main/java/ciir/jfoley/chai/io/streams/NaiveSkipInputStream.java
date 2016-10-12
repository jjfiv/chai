package ciir.jfoley.chai.io.streams;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that knows where it is (tell()) and can skip to absolute references: seek(where).
 * @author jfoley
 */
public class NaiveSkipInputStream extends SkipInputStream {
  private final InputStream inner;
  private long offset;

  public NaiveSkipInputStream(InputStream inner) {
    this.inner = inner;
    this.offset = 0L;
  }
  @Override
  public int read() throws IOException {
    this.offset++;
    return inner.read();
  }

  @Override
  public int read(@Nonnull byte[] b) throws IOException  {
    int amountRead = inner.read(b);
    this.offset += amountRead;
    return amountRead;
  }

  @Override
  public int read(@Nonnull byte[] b, int offset, int amount) throws IOException  {
    int amountRead = inner.read(b, offset, amount);
    this.offset += amountRead;
    return amountRead;
  }

  @Override
  public long tell() throws IOException {
    return this.offset;
  }

  @Override
  public int available() throws IOException {
    return inner.available();
  }

  @Override
  public boolean markSupported() {
    return inner.markSupported();
  }
  @Override
  public void mark(int readLimit) {
    inner.mark(readLimit);
  }
  @Override
  public void reset() throws IOException {
    inner.reset();
  }

  @Override
  public long skip(long delta) throws IOException {
    long actualDelta = inner.skip(delta);
    this.offset += actualDelta;
    return actualDelta;
  }


  @Override
  public void seekRelative(long delta) throws IOException {
    assert(delta >= 0);
    if(delta == 0) return;
    long actualDelta = inner.skip(delta);
    this.offset += actualDelta;
    if(actualDelta < delta) throw new EOFException();
  }

  @Override
  public void close() throws IOException {
    inner.close();
  }
}
