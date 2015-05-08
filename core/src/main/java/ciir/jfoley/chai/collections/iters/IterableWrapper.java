package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.fn.TransformFn;

import java.util.Iterator;

/**
 * @author jfoley
 */
public class IterableWrapper<Y,X> implements Iterable<X> {
  private final Iterable<Y> inner;
  private final TransformFn<Iterator<Y>, Iterator<X>> fn;

  public IterableWrapper(Iterable<Y> inner, TransformFn<Iterator<Y>, Iterator<X>> fn) {
    this.fn = fn;
    this.inner = inner;
  }
  @Override
  public Iterator<X> iterator() {
    return fn.transform(inner.iterator());
  }
}
