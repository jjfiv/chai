package ciir.jfoley.chai.collections.iters;

import java.util.Iterator;

/**
 * @author jfoley.
 */
public interface ClosingIterator<T> extends Iterator<T>, AutoCloseable {
  default Iterable<T> iterable() { return () -> this; }
}
