package ciir.jfoley.chai.fn;

/**
 * @author jfoley
 */
public interface CompareFn<T> {
  boolean compare(T lhs, T rhs);
}
