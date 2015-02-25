package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.io.IO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jfoley
 */
public class BatchedIterator<T> extends ReadOnlyIterator<List<T>> {
  private final Iterator<T> iter;
  private final int size;
  private ArrayList<T> currentBuffer;

  public BatchedIterator(Iterable<T> input, int size) {
    this(input.iterator(), size);
  }
  public BatchedIterator(Iterator<T> iter, int size) {
    this.iter = iter;
    this.size = size;
    currentBuffer = null;
    tryAndFillBuffer();
  }

  void tryAndFillBuffer() {
    if(!iter.hasNext()) {
      currentBuffer = null;
      return;
    }

    currentBuffer = new ArrayList<>(size);
    for (int i = 0; i < size && iter.hasNext(); i++) {
      currentBuffer.add(iter.next());
    }
  }

  @Override
  public void close() throws Exception {
    IO.close(iter);
  }

  @Override
  public boolean hasNext() {
    return currentBuffer != null || iter.hasNext();
  }

  @Override
  public List<T> next() {
    List<T> lastBuffer = currentBuffer;
    tryAndFillBuffer();
    return lastBuffer;
  }
}
