package ciir.jfoley.chai;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.fn.GenerateFn;

/**
 * @author jfoley
 */
public class Timing {
  /**
   * Time an operation that has only side-effects.
   * @param doFn the operation.
   * @return the number of milliseconds taken.
   */
  public static long milliseconds(Runnable doFn) {
    long startTime = System.currentTimeMillis();
    doFn.run();
    long endTime = System.currentTimeMillis();

    return (endTime - startTime);
  }

  /**
   * Time an operation that returns a result.
   * @param genFn the operation.
   * @param <T> the result type.
   * @return a pair of the time taken and the result itself.
   */
  public static <T> Pair<Long, T> milliseconds(GenerateFn<T> genFn) {
    long startTime = System.currentTimeMillis();
    T result = genFn.get();
    long endTime = System.currentTimeMillis();

    return Pair.of(endTime - startTime, result);
  }
}
