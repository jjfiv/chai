package ciir.jfoley.chai.fn;

import ciir.jfoley.chai.Checked;
import ciir.jfoley.chai.lang.Module;

/**
 * @author jfoley.
 */
public class Fns extends Module {
	private static TransformFn identityFn = new TransformFn<Object, Object>() {
		@Override public Object transform(Object input) { return input; }
	};
	private static PredicateFn trueFn = new PredicateFn<Object>() {
		@Override public boolean test(Object input) { return true; }
	};

	public static <T> TransformFn<T,T> identity() {
		return Checked.cast(identityFn);
	}

	public static <T> PredicateFn<T> trueFn() {
		return Checked.cast(trueFn);
	}

}
