package edu.umass.cs.ciir.jfoley.chai.iters;

import java.util.Iterator;

/**
 * @author jfoley.
 */
public interface ClosingIterator<T> extends Iterator<T>, AutoCloseable {
}
