package ciir.jfoley.chai.fn;

/**
 * @author jfoley.
 */
@FunctionalInterface
public interface PredicateFn<T> {
	boolean test(T input);
}
