package ciir.jfoley.chai.io;

import ciir.jfoley.chai.lang.Module;

import java.io.*;
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
      assert(off+amt <= buf.length);
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

  /**
   * Pulls the next byte to see if there is more data to read.
   * @param input a PushBackInputStream
   * @return false if there's no more bytes to read
   * @throws IOException
   */
  public static boolean hasMoreData(PushbackInputStream input) throws IOException {
    try {
      int next = input.read();
      if(next == -1) {
        return false;
      }
      input.unread(next);
      return true;
    } catch (EOFException e) {
      return false;
    }
  }

  /**
   * Tries to use the mark functionality of input streams to see if the next byte read will return EOF or not.
   * @param input a markable input stream or a PushBackInputStream
   * @return false if there's no more bytes to read
   * @throws IOException
   */
  public static boolean hasMoreData(InputStream input) throws IOException {
    if(input instanceof PushbackInputStream) {
      return hasMoreData((PushbackInputStream) input);
    } else if(input.markSupported()) {
      input.mark(1);
      try {
        int next = input.read();
        if (next == -1) return false;
        input.reset();
        return true;
      } catch (EOFException e) {
        return false;
      }
    } else {
      throw new IllegalArgumentException("Don't know how to peek on input stream of class="+input.getClass());
    }
  }



}
