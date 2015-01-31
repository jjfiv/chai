package ciir.jfoley.chai.fn;

/**
 * @author jfoley.
 */
public interface SinkFn<T> {
	public void process(T input);
}
