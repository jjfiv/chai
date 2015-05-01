package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.io.IO;

import java.util.Iterator;

/**
 * @author jfoley
 */
public class MappingIterator<A, B> implements Iterator<B>, AutoCloseable {
  private final TransformFn<A, B> mapFn;
  private final Iterator<A> inner;

  public MappingIterator(Iterator<A> inner, TransformFn<A, B> mapFn) {
    this.inner = inner;
    this.mapFn = mapFn;
  }

  @Override
  public boolean hasNext() {
    return inner.hasNext();
  }

  @Override
  public B next() {
    A maybeNext = inner.next();
    if(maybeNext == null) return null;
    return mapFn.transform(maybeNext);
  }

  @Override
  public void remove() {
    inner.remove();
  }

  @Override
  public void close() throws Exception {
    IO.close(inner);
  }
}
