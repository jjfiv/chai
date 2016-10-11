package ciir.jfoley.chai.fn;

/**
 * @author jfoley.
 */
@FunctionalInterface
public interface SinkFn<T> {
	void process(T input);
}
