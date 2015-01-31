package ciir.jfoley.chai.iters;

import ciir.jfoley.chai.io.IO;

import java.util.Iterator;

/**
 * @author jfoley.
 */
public class PeekIterator<T> extends ReadOnlyIterator<T> {
	private Iterator<T> inner;
	private T current = null;

	public PeekIterator(Iterator<T> inner) {
		this.inner = inner;
		if(inner.hasNext()) {
			this.current = inner.next();
		}
	}

	@Override
	public void close() throws Exception {
		IO.close(inner);
	}

	@Override
	public boolean hasNext() {
		return current != null;
	}

	@Override
	public T next() {
		T prev = current;
		if(inner.hasNext()) {
			current = inner.next();
		} else {
			current = null;
		}
		return prev;
	}

	public T peek() {
		return current;
	}
}
