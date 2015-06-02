package ciir.jfoley.chai.lang;

/**
 * This class contains the "no, seriously, assert, and don't turn this off."
 * @author jfoley
 */
public class Require {

  /**
   * Use JVM assert if we can, otherwise throw an error anyway.
   * @param cond the condition under test.
   */
  public static void that(boolean cond) {
    assert(cond);
    //noinspection ConstantConditions
    if(!cond) throw new AssertionError("Requirement failure.");
  }

  /**
   * Use JVM assert if we can, otherwise throw an error anyway.
   * @param cond the condition under test.
   */
  public static void that(boolean cond, String msg) {
    assert(cond) : msg;
    //noinspection ConstantConditions
    if(!cond) throw new AssertionError(msg);
  }
}
