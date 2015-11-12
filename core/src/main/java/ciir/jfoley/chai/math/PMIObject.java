package ciir.jfoley.chai.math;

import javax.annotation.Nonnull;

/**
 * @author jfoley
 */
public class PMIObject<T> implements Comparable<PMIObject<?>> {
  public final T term;
  public final double pmi;

  public PMIObject(T term, double termFrequency, double queryFrequency, double queryProxFrequency, double collectionLength) {
    this.term = term;

    double px = termFrequency / collectionLength;
    double py = queryFrequency / collectionLength;
    double pxy = queryProxFrequency / collectionLength;
    this.pmi = Math.log(pxy) - Math.log(px) + Math.log(py);
  }

  @Override
  public String toString() {
    return "<"+term+" "+pmi+">";
  }

  @Override
  public int compareTo(@Nonnull PMIObject<?> o) {
    return Double.compare(pmi, o.pmi);
  }
}
