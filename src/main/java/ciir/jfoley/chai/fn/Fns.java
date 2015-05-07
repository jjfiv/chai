package ciir.jfoley.chai.fn;

import ciir.jfoley.chai.Checked;
import ciir.jfoley.chai.lang.Module;

/**
 * @author jfoley.
 */
public class Fns extends Module {
	private static Object identityFn = new TransformFn<Object, Object>() {
		@Override
		public Object transform(Object input) {
			return input;
		}
	};

	public static <T> TransformFn<T,T> identity() {
		return Checked.cast(identityFn);
	}
}
