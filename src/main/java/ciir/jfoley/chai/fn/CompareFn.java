package ciir.jfoley.chai.fn;

/**
 * @author jfoley
 */
@FunctionalInterface
public interface CompareFn<T> {
  boolean compare(T lhs, T rhs);
}
