package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.util.List;

/**
 * @author jfoley
 * @deprecated Use the {@link RankingMeasures} class instead...
 */
@Deprecated
public class AUC {
  /**
   * Note still a lingering issue w.r.t. how to break ties. This is okay because in practice, w.r.t. to classification instances, it is really unlikely that we will find ties.
   * @param inPoints list of (true judgment, score) pairs
   * @return area under the ROC curve, estimated by trapezoidal approx.
   */
  @Deprecated
  public static double compute(List<Pair<Boolean, Double>> inPoints) {
    return RankingMeasures.computeAUC(inPoints);
  }

  /** Returns a cutoff that maximizes accuracy. */
  @Deprecated
  public static double maximizeAccuracy(List<Pair<Boolean, Double>> inPoints) {
    return RankingMeasures.maximizeAccuracy(inPoints);
  }

  @Deprecated
  public static double maximizeF1(List<Pair<Boolean, Double>> inPoints) {
    return RankingMeasures.maximizeFScore(inPoints, 1.0);
  }
  @Deprecated
  public static double maximizeFScore(List<Pair<Boolean, Double>> inPoints, double beta) {
    return RankingMeasures.maximizeFScore(inPoints, beta);
  }

  @Deprecated
  public static double computePrec(List<Pair<Boolean, Double>> data, int cutoff) {
    return RankingMeasures.computePrec(data, cutoff);
  }
  @Deprecated
  public static double computeAP(List<Pair<Boolean, Double>> data) {
    return RankingMeasures.computeAP(data);
  }
  @Deprecated
  public static double computeAP(List<Pair<Boolean, Double>> data, int numRelevant) {
    return RankingMeasures.computeAP(data, numRelevant);
  }
}
