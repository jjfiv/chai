package ciir.jfoley.chai.fn;

/**
 * @author jfoley.
 */
public interface SinkFn<T> {
	void process(T input);
}
