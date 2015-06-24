package ciir.jfoley.chai.io;

import ciir.jfoley.chai.collections.iters.UntilNullIterator;

import java.io.*;
import java.util.Iterator;

/**
 * @author jfoley.
 */
public class LinesIterable implements Iterable<String>, Closeable {

	private final BufferedReader reader;
	private int lineNumber;

	public LinesIterable(BufferedReader reader) {
		lineNumber = 0;
		this.reader = reader;
	}

	@Override
	public Iterator<String> iterator() {
		return new UntilNullIterator<>(() -> {
      try {
				return this.readLine();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
	}

	public String readLine() throws IOException {
		++this.lineNumber;
		return reader.readLine();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public static LinesIterable of(BufferedReader reader) {
		return new LinesIterable(reader);
	}
	public static LinesIterable of(Reader reader) {
		return new LinesIterable(new BufferedReader(reader));
	}
	public static LinesIterable of(String data) {
		return of(new StringReader(data));
	}

	public static LinesIterable fromFile(File path) throws IOException {
    return fromFile(path.getAbsolutePath());
	}

	public static LinesIterable fromFile(String path) throws IOException {
		return of(IO.openReader(path));
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
