package ciir.jfoley.chai.codec;

/**
 * @author jfoley.
 */
public interface Codec<INNER, OUTER> {
	OUTER wrap(INNER inner);
	INNER unwrap(OUTER outer);
}
