package ciir.jfoley.chai.lang;

/**
 * This is a class marker that helps explain how to use a complicated class.
 * @author jfoley
 */
public interface Builder<T> {
  /**
   * Build and get output.
   * @return the built item of type T.
   */
  T getOutput();
}
