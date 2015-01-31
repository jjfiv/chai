package ciir.jfoley.chai.codec;

/**
 * @author jfoley.
 */
public interface Codec<INNER, OUTER> {
	public OUTER wrap(INNER inner);
	public INNER unwrap(OUTER outer);
}
