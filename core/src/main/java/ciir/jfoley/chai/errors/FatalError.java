package ciir.jfoley.chai.errors;

/**
 * @author jfoley.
 */
public class FatalError extends RuntimeException {
	public FatalError(Exception e) {
		super(e);
	}

	public FatalError(String msg) {
		super(msg);
	}
}
