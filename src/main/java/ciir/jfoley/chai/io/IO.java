package ciir.jfoley.chai.io;

import ciir.jfoley.chai.errors.FatalError;

import java.io.Closeable;

/**
 * @author jfoley.
 */
public class IO {

	/** Close anything! */
	public static void close(Object obj) {
		try {
			if (obj != null) {
				if (obj instanceof Closeable) {
					((Closeable) obj).close();
				} else if (obj instanceof AutoCloseable) {
					((AutoCloseable) obj).close();
				}
			}
		} catch (Exception ex) {
			throw new FatalError(ex);
		}
	}


}
