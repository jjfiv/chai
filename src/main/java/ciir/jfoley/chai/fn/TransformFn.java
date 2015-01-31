package ciir.jfoley.chai.fn;

/**
 * A -> B
 * @author jfoley.
 */
public interface TransformFn<A,B> {
	public B transform(A input);
}
