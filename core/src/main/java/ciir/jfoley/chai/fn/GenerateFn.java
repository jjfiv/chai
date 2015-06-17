package ciir.jfoley.chai.fn;

/**
 * An interface that generates something.
 * @author jfoley.
 */
@FunctionalInterface
public interface GenerateFn<T> {
	T get();
}
