package edu.umass.cs.ciir.jfoley.chai.io;

import edu.umass.cs.ciir.jfoley.chai.iters.UntilNullIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author jfoley.
 */
public class LinesIterable implements Iterable<String> {

	private final BufferedReader reader;

	public LinesIterable(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public Iterator<String> iterator() {
		return new UntilNullIterator<>(() -> {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
