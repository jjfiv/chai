package ciir.jfoley.chai.fn;

import java.io.IOException;

/**
 * A -> B
 * @author jfoley.
 */
public interface TransformFn<A,B> {
	public B transform(A input);
}
