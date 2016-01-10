/**
 * Probably MIT licenced, as the blog links to a Julia version (https://github.com/johnmyleswhite/StreamStats.jl) which has an MIT license on it, despite the code on the blog not having a license.
 */
package ciir.jfoley.chai.math;

import ciir.jfoley.chai.collections.ArrayListMap;
import ciir.jfoley.chai.fn.SinkFn;

import java.util.List;
import java.util.Map;

/**
 * Ideas taken from:
 * http://www.johndcook.com/blog/standard_deviation/
 * @author jfoley
 */
public final class StreamingStats implements SinkFn<Double> {
  private long numberOfElements;
  private double mean;
  private double sValue;
  private double max;
  private double min;
  private double total;

  public StreamingStats() {
    clear();
  }
  public final void push(double x) {
    numberOfElements++;

    // set up for next iteration
    double oldMean = mean;
    double oldS = sValue;

    max = Math.max(max, x);
    min = Math.min(min, x);
    total += x;

    // See Knuth TAOCP vol 2, 3rd edition, page 232
    if (numberOfElements == 1) {
      mean = x;
      return;
    }

    mean = oldMean + (x - oldMean)/ ((double) numberOfElements);
    sValue = oldS + (x - oldMean)*(x - mean);
  }

  /**
   * Not lossless. The streaming method does better.
   * @param other built up statistics.
   */
  public final void add(StreamingStats other) {
    long total = this.numberOfElements + other.numberOfElements;
    double lhsFrac = this.numberOfElements / (double) total;
    double rhsFrac = other.numberOfElements / (double) total;

    double newMean = lhsFrac * this.mean + rhsFrac * other.mean;

    double delta = other.mean - this.mean;
    double delta2 = delta * delta;

    double newS = this.sValue + other.sValue + (delta2 * lhsFrac * rhsFrac);

    this.numberOfElements = total;
    this.mean = newMean;
    this.sValue = newS;
    this.max = Math.max(this.max, other.max);
    this.min = Math.min(this.min, other.min);
    this.total += other.total;
  }

  public final void clear() {
    total = 0;
    numberOfElements = 0;
    mean = sValue = 0;
    max = -Double.MAX_VALUE;
    min = Double.MAX_VALUE;
  }
  public final double getMean() {
    return mean;
  }
  public final double getVariance() {
    if(numberOfElements <= 1) return 0.0;
    return sValue / (double) (numberOfElements - 1);
  }
  public final double getStandardDeviation() { return Math.sqrt(getVariance()); }
  public final double getMax() {
    return max;
  }
  public final double getMin() {
    return min;
  }
  public final double getTotal() {
    return total;
  }
  public final double getCount() { return numberOfElements; }

  public final long count() {
    return numberOfElements;
  }

  public final Map<String, Double> features() {
    Map<String,Double> results = new ArrayListMap<>();
    results.put("mean", getMean());
    results.put("variance", getVariance());
    results.put("stddev", getStandardDeviation());
    results.put("max", getMax());
    results.put("min", getMin());
    results.put("total", getTotal());
    results.put("count", getCount());
    return results;
  }

  @Override
  public String toString() {
    return features().toString();
  }

  @Override
  public final void process(Double input) {
    push(input);
  }

  public final void pushAll(float[] data) {
    for (float v : data) { push(v); }
  }

  public void pushAll(List<Double> select) {
    for (Double x : select) {
      push(x);
    }
  }
}
