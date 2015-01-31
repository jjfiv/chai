package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.io.IO;

import java.util.Iterator;

/**
* @author jfoley.
*/
public class OneShotIterable<T> implements Iterable<T>, AutoCloseable {
	private Iterator<T> iter;
	public OneShotIterable(Iterator<T> iter) {
		this.iter = iter;
	}

	@Override
	public Iterator<T> iterator() {
		if(iter == null) throw new IllegalStateException("Can't use a OneShotIterable twice!");
		Iterator<T> prev = iter;
		iter = null;
		return prev;
	}

	@Override
	public void close() throws Exception {
		IO.close(iter);
	}
}
