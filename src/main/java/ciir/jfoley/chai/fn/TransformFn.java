package ciir.jfoley.chai.fn;

/**
 * A function interface that maps from A -&gt; B. Because the whole world hasn't made it to Java 8 yet.
 * @author jfoley.
 */
@FunctionalInterface
public interface TransformFn<A,B> {
	B transform(A input);
}
