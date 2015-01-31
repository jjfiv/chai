package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.io.IO;

import java.util.Iterator;

/**
 * @author jfoley.
 */
public class FilteringIterator<T> extends ReadOnlyIterator<T> {

	private final PeekIterator<T> inner;
	private final PredicateFn<T> fn;

	public FilteringIterator(Iterator<T> inner, PredicateFn<T> fn) {
		this.inner = new PeekIterator<>(inner);
		this.fn = fn;
		seek();
	}

	public void seek() {
		while(inner.hasNext() && !fn.test(inner.peek())) {
			inner.next();
		}
	}

	@Override
	public void close() throws Exception {
		IO.close(inner);
	}

	@Override
	public boolean hasNext() {
		seek();
		return inner.hasNext();
	}

	@Override
	public T next() {
		seek();
		return inner.next();
	}
}
