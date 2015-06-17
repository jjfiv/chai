package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.fn.CompareFn;
import ciir.jfoley.chai.io.IO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author jfoley
 */
public class GroupByIterator<T> extends ReadOnlyIterator<List<T>> {
  private final PeekIterator<T> inner;
  private final CompareFn<T> inSameGroupFn;
  private ArrayList<T> buffer;

  public GroupByIterator(Iterator<T> inner, CompareFn<T> inSameGroupFn) {
    this.inner = new PeekIterator<>(inner);
    this.buffer = new ArrayList<>();
    this.inSameGroupFn = inSameGroupFn;
    fillBuffer();
  }

  public GroupByIterator(Iterator<T> inner) {
    this(inner, Objects::equals);
  }

  private void fillBuffer() {
    if(!inner.hasNext()) {
      buffer = null;
      return;
    }

    buffer.clear();
    T prev = null;
    while(inner.hasNext()) {
      if (prev == null || inSameGroupFn.compare(prev, inner.peek())) {
        prev = inner.next();
        this.buffer.add(prev);
      } else {
        break;
      }
    }
  }

  @Override
  public void close() throws Exception {
    IO.close(inner);
  }

  @Override
  public boolean hasNext() {
    return buffer != null;
  }

  @Override
  public List<T> next() {
    List<T> prevBuffer = buffer;
    fillBuffer();
    return prevBuffer;
  }
}
