package ciir.jfoley.chai.collections.sorted;

import java.io.Closeable;

/**
 * @author jfoley
 */
public interface SortedReader<T extends Comparable<T>> extends Closeable, Comparable<SortedReader<T>> {
  T peek();
  T next();
  default boolean hasNext() { return peek() != null; }

  @Override
  default int compareTo(SortedReader<T> o) {
    return peek().compareTo(o.peek());
  }
}
