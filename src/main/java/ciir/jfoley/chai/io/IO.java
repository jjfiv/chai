package ciir.jfoley.chai.io;

import ciir.jfoley.chai.errors.FatalError;

import java.io.*;

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


	public static String slurp(File path) throws IOException {
		final StringBuilder contents = new StringBuilder();
		try (LinesIterable lines = LinesIterable.fromFile(path)) {
			for (String line : lines) {
				contents.append(line).append('\n');
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
		return contents.toString();
	}

	public static void spit(String data, File output) throws IOException {
		try (PrintWriter pw = openPrintWriter(output.getAbsolutePath())) {
			pw.print(data);
		}
	}

	public static InputStream openInputStream(String file) throws IOException {
		return CompressionCodec.openInputStream(file);
	}
	public static OutputStream openOutputStream(String file) throws IOException {
		return CompressionCodec.openOutputStream(file);
	}

	public static PrintWriter openPrintWriter(String file) throws IOException {
		return new PrintWriter(openOutputStream(file));
	}

}
