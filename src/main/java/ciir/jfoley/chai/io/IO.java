package ciir.jfoley.chai.io;

import ciir.jfoley.chai.Encodings;
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

	public static int BUFFER_SIZE = 8192;

	public static String readAll(Reader reader) throws IOException {
		final StringBuilder contents = new StringBuilder();
		char buf[] = new char[BUFFER_SIZE];
		while(true) {
			int amt = reader.read(buf);
			if(amt < 0) break;
			contents.append(buf, 0, amt);
			if(amt < buf.length) break;
		}
		return contents.toString();
	}

	public static String slurp(File path) throws IOException {
		try(Reader reader = openReader(path.getAbsolutePath())) {
			return readAll(reader);
		}
	}

	public static void spit(String data, File output) throws IOException {
		try (PrintWriter pw = openPrintWriter(output.getAbsolutePath())) {
			pw.print(data);
		}
	}

	public static Reader openReader(String file) throws IOException {
		return new InputStreamReader(openInputStream(file), Encodings.UTF8);
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
