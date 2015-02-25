package ciir.jfoley.chai.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley.
 */
public class Streams {
  public static void copy(InputStream src, OutputStream dest, int bufferSize) throws IOException {
    byte[] buf = new byte[bufferSize];
    while(true) {
      int amt = src.read(buf);
      if(amt == -1) break;
      dest.write(buf, 0, amt);
    }
  }
	public static void copy(InputStream src, OutputStream dest) throws IOException {
    copy(src, dest, IO.BUFFER_SIZE);
	}
}
