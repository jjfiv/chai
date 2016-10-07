package ciir.jfoley.chai.collections.sorted;

import java.io.Closeable;

/**
 * @author jfoley
 */
public interface SortedReader<T> extends Closeable {
  T peek();
  T next();
  default boolean hasNext() { return peek() != null; }
}
