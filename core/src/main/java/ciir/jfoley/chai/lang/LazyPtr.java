package ciir.jfoley.chai.lang;

import ciir.jfoley.chai.fn.GenerateFn;

/**
 * A wrapper class that hides a pointer to a lazy variable. It gets initialized upon first call to get().
 * Note: this class is *not* threadsafe.
 * @author jfoley
 */
public class LazyPtr<T> implements GenerateFn<T> {
  private final GenerateFn<T> createFn;
  private T instance;
  public LazyPtr(GenerateFn<T> createFn) {
    this.createFn = createFn;
    this.instance = null;
  }
  public T get() {
    if(instance == null) {
      instance = createFn.get();
      if(instance == null) throw new NullPointerException("LazyPtr<T> generator gave us NULL!");
    }
    return instance;
  }
}
