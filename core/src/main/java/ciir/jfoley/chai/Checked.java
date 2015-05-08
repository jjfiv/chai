package ciir.jfoley.chai;

import ciir.jfoley.chai.lang.Module;

/**
 * @author jfoley.
 */
public class Checked extends Module {
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object input) {
		return (T) input;
	}
}
