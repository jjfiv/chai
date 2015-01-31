package edu.umass.cs.ciir.jfoley.chai.iters;

import java.util.Iterator;

/**
* @author jfoley.
*/
public abstract class ReadOnlyIterator<T> implements Iterator<T> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
