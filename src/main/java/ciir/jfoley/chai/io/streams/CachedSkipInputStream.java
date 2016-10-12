package ciir.jfoley.chai.io.streams;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * NOTE: skipping is untested and probably broken.
 * @author jfoley
 */
public class CachedSkipInputStream extends SkipInputStream {
  private static final int BufferSize = 8192;

  private final SkipInputStream inner;

  // double-buffered solution; current and unused
  byte[] unused;
  byte[] data;

  long bufferStartPosition;
  int bufferIndex;
  int bufferFill;
  final int bufferSize;

  public CachedSkipInputStream(SkipInputStream inner) {
    this(inner, BufferSize);
  }
  public CachedSkipInputStream(SkipInputStream inner, int bufferSize) {
    this.inner = inner;
    this.bufferSize = bufferSize;
    this.data = new byte[bufferSize];
    this.unused = new byte[bufferSize];
    this.bufferIndex = 0;
    this.bufferFill = 0;
    this.bufferStartPosition = 0;
  }

  @Override
  public int read() throws IOException {
    if (bufferIndex < bufferFill) {
      return data[bufferIndex++] & 0xff;
    }
    fillBuffer();
    if(bufferIndex < bufferFill) {
      return data[bufferIndex++] & 0xff;
    }
    assert (bufferFill == 0);
    return -1;
  }

  /**
   * Todo, make this faster someday.
   */
  @Override
  public int read(@Nonnull byte[] target, int offset, int amount) throws IOException {
    for (int i = 0; i < amount; i++) {
      int nextc = read();
      if(nextc == -1) {
        if(i == 0) return -1;
        return i;
      }
      target[i+offset] = (byte) (nextc & 0xff);
    }
    return amount;
  }

  private void swapBuffers() {
    byte[] tmp = unused;
    unused = data;
    data = tmp;
  }

  /**
   * Fill buffer and update local variables.
   */
  private void fillBuffer() throws IOException {
    // step over the consumed bytes so far:
    bufferStartPosition += bufferIndex;

    // copy any bytes we haven't skipped or read:
    int fill = 0;
    for (int i = bufferIndex; i < bufferFill; i++) {
      unused[fill++] = data[i];
    }

    // reset our buffer pointers:
    bufferIndex = 0;
    bufferFill = fill;
    swapBuffers();

    // try and fill buffer from underlying stream now:
    int requested = bufferSize - bufferFill;
    int amountRead = inner.read(data, fill, requested);
    if(amountRead == -1) {
      return;
    }
    bufferFill += amountRead;
  }

  @Override
  public long tell() throws IOException {
    return this.bufferStartPosition + bufferIndex;
  }

  @Override
  public void seekRelative(long delta) throws IOException {
    if(delta < 0) {
      seekBackwards(-delta);
    } else {
      long start = tell();
      seekForwards(delta);
      assert(tell() == start+delta);
    }
  }

  /**
   *
   * @param delta positive number of bytes to rewind:
   */
  private void seekBackwards(long delta) throws IOException {
    // are we within the same buffer? if so, don't do any I/O
    if(delta <= bufferIndex) {
      bufferIndex -= delta;
      return;
    }

    //System.err.printf("seekBackwards: @%d|%d [%d/%d] -%d\n", inner.tell(), tell(), bufferIndex, bufferFill, delta);
    // discard buffer, underlying seek:
    inner.seekRelative(-bufferFill-delta);
    this.bufferFill = 0;
    this.bufferIndex = 0;
    this.bufferStartPosition = inner.tell();
    //fillBuffer();
    //System.err.printf("!seekBackwards: @%d|%d [%d/%d] -%d\n", inner.tell(), tell(), bufferIndex, bufferFill, delta);
  }

  private void seekForwards(long delta) throws IOException {
    int remaining = bufferFill - bufferIndex;
    if(delta < remaining) {
      bufferIndex += delta;
      return;
    }

    //System.err.printf("seekForwards: @%d|%d [%d/%d] %d\n", inner.tell(), tell(), bufferIndex, bufferFill, delta);
    // discard buffer, underlying seek:
    inner.seek(tell() + delta);
    this.bufferFill = 0;
    this.bufferIndex = 0;
    this.bufferStartPosition = inner.tell();
    //fillBuffer();
    //System.err.printf("!seekForwards: @%d|%d [%d/%d] %d\n", inner.tell(), tell(), bufferIndex, bufferFill, delta);
  }

  @Override
  public void close() throws IOException {
    this.data = null;
    this.unused = null;
    inner.close();
  }
}
