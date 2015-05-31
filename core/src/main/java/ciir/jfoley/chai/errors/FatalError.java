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


	public static interface Operation {
		public void perform() throws Exception;
	}
	public static void wrap(Operation op) {
		try {
			op.perform();
		} catch (Exception e) {
			throw new FatalError(e);
		}
	}
}
