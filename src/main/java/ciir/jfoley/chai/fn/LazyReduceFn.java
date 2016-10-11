package ciir.jfoley.chai.fn;

/**
 * @author jfoley
 */
public interface LazyReduceFn<T> {
  boolean shouldReduce(T lhs, T rhs);
	T reduce(T lhs, T rhs);
}
