package ciir.jfoley.chai.lang;

import ciir.jfoley.chai.fn.GenerateFn;

/**
 * Does what it says on the box. Tries to avoid locking in the already-init case.
 * @author jfoley.
 */
public class ThreadsafeLazyPtr<T> implements GenerateFn<T> {
  private final GenerateFn<T> createFn;
  private T instance;
  public ThreadsafeLazyPtr(GenerateFn<T> createFn) {
    this.createFn = createFn;
    this.instance = null;
  }
  public T get() {
    if(instance != null) return instance;
    synchronized (this) {
      if (instance == null) {
        instance = createFn.get();
        if (instance == null) throw new NullPointerException("ThreadsafeLazyPtr<T> generator gave us NULL!");
      }
    }
    return instance;
  }
}
