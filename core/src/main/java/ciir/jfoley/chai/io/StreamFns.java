package ciir.jfoley.chai.io;

import ciir.jfoley.chai.lang.Module;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * @author jfoley
 */
public class StreamFns extends Module {
  /**
   * For when you want to read ''amt'' bytes from an InputStream, no matter how many syscalls it takes.
   * @param is the input stream to read from.
   * @param amt the number of bytes to read.
   * @return a byte array filled with the next amt bytes from the input stream.
   * @throws EOFException if done
   * @throws IOException if the stream complains
   */
  public static byte[] readBytes(InputStream is, int amt) throws IOException {
    byte[] buf = new byte[amt];

    // Begin I/O loop:
    int off = 0;
    while(true) {
      int read = is.read(buf, off, amt);
      if (read < -1) {
        throw new EOFException();
      }
      if(read == amt) break;

      // Ugh; try again
      off += read;
      amt -= read;
    }
    return buf;
  }

  public static ByteBuffer readChannel(ReadableByteChannel channel, int amt) throws IOException {
    ByteBuffer data = ByteBuffer.allocate(amt);
    // Begin I/O loop:
    while(true) {
      int read = channel.read(data);
      if (read < -1) {
        throw new EOFException();
      }
      if(read == amt) break;

      // Ugh; try again
      amt -= read;
    }
    return data;
  }

  /**
   * This method creates an input stream from a ByteBuffer.
   * @param bb the byte buffer.
   * @return the wrapped input stream.
   */
  public static InputStream fromByteBuffer(ByteBuffer bb) {
    // TODO, add a ByteBufferInputStream implementation from a trusted source: e.g. http://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream
    return new ByteArrayInputStream(bb.array());
  }

  /**
   * This method reads a ByteBuffer into a byte[], using {:link fromByteBuffer}
   * @param buffer the bytebuffer to read all from.
   * @return a byte[] containing all the data in the ByteBuffer.
   * @throws IOException if something goes wrong with the streams.
   */
  public static byte[] readAll(ByteBuffer buffer) throws IOException {
    InputStream str = fromByteBuffer(buffer);
    return readBytes(str, buffer.limit());
  }
}
