package ciir.jfoley.chai.math;

import ciir.jfoley.chai.lang.Module;

/**
 * Some DoubleArrayFns for now.
 * @author jfoley
 */
public class Stats extends Module {
  public static double mean(double[] values) {
    double sum = 0.0;
    for (double value : values) {
      sum += value;
    }
    return sum / (double) values.length;
  }

  public static double variance(double[] values) {
    double mean = mean(values);
    double diffsum = 0.0;
    for (double value : values) {
      double diff = (mean - value);
      diffsum += diff*diff;
    }
    return diffsum / (double) (values.length - 1);
  }

  public static double standardDeviation(double[] values) {
    return Math.sqrt(variance(values));
  }
}
