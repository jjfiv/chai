package edu.umass.cs.ciir.jfoley.chai.iters;

/**
* @author jfoley.
*/
public abstract class ReadOnlyIterator<T> implements ClosingIterator<T> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
