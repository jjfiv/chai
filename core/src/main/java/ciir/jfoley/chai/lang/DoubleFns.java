package ciir.jfoley.chai.lang;

/**
 * Utility functions for manipulating doubles.
 * @author jfoley
 */
public class DoubleFns {
  /**
   * Compare two doubles for near-equality; the useful kind.
   * @param a the first number;
   * @param b the second number;
   * @param epsilon the difference you'll tolerate as being equivalent. (Must be positive).
   * @return |a - b| &lt; epsilon;
   */
  public static boolean equals(double a, double b, double epsilon) {
    assert(epsilon > 0);
    return Math.abs(a - b) < epsilon;
  }
}
