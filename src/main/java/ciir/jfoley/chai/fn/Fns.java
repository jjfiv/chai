package ciir.jfoley.chai.fn;

import ciir.jfoley.chai.Checked;
import ciir.jfoley.chai.lang.Module;

/**
 * @author jfoley.
 */
public class Fns extends Module {
  private static TransformFn identityFn = input -> input;
  private static PredicateFn trueFn = input -> true;

  public static <T> TransformFn<T,T> identity() {
    return Checked.cast(identityFn);
  }

  public static <T> PredicateFn<T> trueFn() {
    return Checked.cast(trueFn);
  }

}
