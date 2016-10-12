package ciir.jfoley.chai.coders.data;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * @author jfoley
 */
public interface DataChunk extends Closeable {
  /** Not required for functionality, but useful for debugging. */
  int getByte(int index);

  /** Return the total number of bytes represented by this buffer. */
  long byteCount();
  /** Return the data inside this buffer as a ByteBuffer. */
  ByteBuffer asByteBuffer();
  /** Return the data inside as a byte array if at all possible. */
  byte[] asByteArray();
  /** Return the data inside this as an InputStream. */
  InputStream asInputStream();
  /** Write the data to the given OutputStream */
  void write(OutputStream out) throws IOException;
  /** Write the data to the given WritableByteChannel */
  void write(WritableByteChannel out) throws IOException;

}
