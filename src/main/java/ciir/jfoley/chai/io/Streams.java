package ciir.jfoley.chai.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley.
 */
public class Streams {
	public static void copy(InputStream src, OutputStream dest) throws IOException {
		byte[] buf = new byte[8192];
		while(true) {
			int amt = src.read(buf);
			if(amt == -1) break;
			dest.write(buf, 0, amt);
		}
	}
}
