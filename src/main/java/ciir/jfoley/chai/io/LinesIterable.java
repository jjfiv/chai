package ciir.jfoley.chai.io;

import ciir.jfoley.chai.collections.iters.UntilNullGenerator;
import ciir.jfoley.chai.collections.iters.UntilNullIterator;

import java.io.*;
import java.util.Iterator;

/**
 * @author jfoley.
 */
public class LinesIterable implements Iterable<String>, AutoCloseable {

	private final BufferedReader reader;

	public LinesIterable(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public Iterator<String> iterator() {
		return new UntilNullIterator<>(new UntilNullGenerator<String>() {
			@Override
			public String next() {
				try {
					return reader.readLine();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public void close() throws Exception {
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

	public static LinesIterable fromFile(File path) throws FileNotFoundException {
		return of(new FileReader(path));
	}

	public static LinesIterable fromFile(String path) throws FileNotFoundException {
		return of(new FileReader(path));
	}
}
