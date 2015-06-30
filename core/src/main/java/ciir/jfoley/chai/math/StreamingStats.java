/**
 * Probably MIT licenced, as the blog links to a Julia version (https://github.com/johnmyleswhite/StreamStats.jl) which has an MIT license on it, despite the code on the blog not having a license.
 */
package ciir.jfoley.chai.math;

import ciir.jfoley.chai.collections.ArrayListMap;
import ciir.jfoley.chai.fn.SinkFn;

import java.util.Map;

/**
 * Ideas taken from:
 * http://www.johndcook.com/blog/standard_deviation/
 * @author jfoley
 */
public class StreamingStats implements SinkFn<Double> {
  private long numberOfElements;
  private double oldMean;
  private double newMean;
  private double oldS;
  private double newS;
  private double max;
  private double min;
  private double total;

  public StreamingStats() {
    clear();
  }
  public void push(double x) {
    numberOfElements++;

    max = Math.max(max, x);
    min = Math.min(min, x);
    total += x;

    // See Knuth TAOCP vol 2, 3rd edition, page 232
    if (numberOfElements == 1) {
      oldMean = newMean = x;
      oldS = 0.0;
      return;
    }

    newMean = oldMean + (x - oldMean)/ ((double) numberOfElements);
    newS = oldS + (x - oldMean)*(x - newMean);

    // set up for next iteration
    oldMean = newMean;
    oldS = newS;
  }
  public void clear() {
    numberOfElements = 0;
    oldMean = newMean = oldS = newS = 0;
    max = -Double.MAX_VALUE;
    min = Double.MAX_VALUE;
  }
  public double getMean() {
    return numberOfElements > 0 ? newMean : 0.0;
  }
  public double getVariance() {
    if(numberOfElements <= 1) return 0.0;
    return newS / (double) (numberOfElements - 1);
  }
  public double getStandardDeviation() { return Math.sqrt(getVariance()); }
  public double getMax() {
    return max;
  }
  public double getMin() {
    return min;
  }
  public double getTotal() {
    return total;
  }
  public double getCount() { return numberOfElements; }

  public long count() {
    return numberOfElements;
  }

  public Map<String, Double> features() {
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
  public void process(Double input) {
    push(input);
  }
}
