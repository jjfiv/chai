package ciir.jfoley.chai.collections.iters;

/**
* @author jfoley.
*/
public abstract class ReadOnlyIterator<T> implements ClosingIterator<T> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
