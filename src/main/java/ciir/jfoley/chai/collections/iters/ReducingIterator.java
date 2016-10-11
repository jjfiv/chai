package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.fn.LazyReduceFn;
import ciir.jfoley.chai.io.IO;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jfoley
 */
public class ReducingIterator<T> implements ClosingIterator<T> {
  private final Iterator<T> inner;
  private final LazyReduceFn<T> reducer;
  private T current;
  private T next;

  public ReducingIterator(Iterator<T> inner, LazyReduceFn<T> reducer) {
    this.reducer = reducer;
    this.inner = inner;
    current = pullNext();
    next = null;
    updateCurrentAndNext();
  }

  T pullNext() {
    return inner.hasNext() ? inner.next() : null;
  }

  void updateCurrentAndNext() {
    while(true) {
      next = pullNext();
      if(next == null) break;
      if(reducer.shouldReduce(current, next)) {
        current = reducer.reduce(current, next);
      } else break;
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
    if(!hasNext()) throw new NoSuchElementException();
    T out = current;
    current = next;
    updateCurrentAndNext();
    return out;
  }

  public static <T> Iterable<T> of(Iterable<T> input, LazyReduceFn<T> reducer) {
    return () -> new ReducingIterator<>(input.iterator(), reducer);
  }
}

