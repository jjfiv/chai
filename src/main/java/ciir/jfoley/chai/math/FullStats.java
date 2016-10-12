package ciir.jfoley.chai.math;

import gnu.trove.list.array.TDoubleArrayList;

import java.util.Map;

/**
 * For when you want percentiles; need to keep them around since the streaming versions of percentiles aren't great and are hard to code.
 * @author jfoley
 */
public class FullStats {
  public TDoubleArrayList observed = new TDoubleArrayList();
  public StreamingStats summaryStats = new StreamingStats();
  public boolean sorted = false;

  public int getN() {
    return Math.toIntExact(summaryStats.getN());
  }

  public void push(double val) {
    observed.add(val);
    summaryStats.push(val);
    sorted = false;
  }

  public double getPercentile(double n) {
    if(!sorted) {
      observed.sort();
      sorted = true;
    }
    int N = getN();
    int index = (int) Math.min((N * (n / 100.0)), N-1);
    return observed.get(index);
  }

  public double getMedian() {
    return getPercentile(50);
  }

  public Map<String, Double> features() {
    Map<String, Double> start = summaryStats.features();
    start.put("5th", getPercentile(5));
    start.put("median", getPercentile(50));
    start.put("95th", getPercentile(95));
    return start;
  }

  public final double getMean() { return summaryStats.getMean(); }
  public final double getVariance() { return summaryStats.getVariance(); }
  public final double getStandardDeviation() { return summaryStats.getStandardDeviation(); }
  public final double getMax() { return summaryStats.getMax(); }
  public final double getMin() { return summaryStats.getMin(); }
  public final double getTotal() { return summaryStats.getTotal(); }
  public final double getCount() { return summaryStats.getCount(); }
  public final long count() { return observed.size(); }

    @Override
  public String toString() {
    return features().toString();
  }
}
