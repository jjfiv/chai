package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.util.ListFns;

import java.util.Comparator;
import java.util.List;

/**
 * @author jfoley
 */
public class RankingMeasures {
  public enum TieBreaking {
    WORST_CASE, // if we assume all ties are broken in the worst way (positives lowest in ranking)
    BEST_CASE // if we assume all ties are broken in the best way (positives highest in ranking)
  }

  public static Comparator<PredTruth> getComparator(TieBreaking kind) {
    switch (kind) {
      case WORST_CASE:
        return PredTruth.scoreTiesWorstCase;
      case BEST_CASE:
        return PredTruth.scoreTiesBestCase;
      default: throw new IllegalArgumentException(kind.toString());
    }
  }

  /**
   * Note still a lingering issue w.r.t. how to break ties. This is okay because in practice, w.r.t. to classification instances, it is really unlikely that we will find ties.
   *  TieBreaking can now be manually specified, default to WORST_CASE "pessimistic", because truly anything we predict from the set of instances with equal scores will just be luck. Should not reward our AUC with this.
   * @param inPoints list of (true judgment, score) pairs
   * @return area under the ROC curve, estimated by trapezoidal approx.
   */
  public static double computeAUC(List<Pair<Boolean, Double>> inPoints) {
    return computeAUC(inPoints, TieBreaking.WORST_CASE);
  }
  /**
   * Note still a lingering issue w.r.t. how to break ties. This is okay because in practice, w.r.t. to classification instances, it is really unlikely that we will find ties.
   * @param inPoints list of (true judgment, score) pairs
   * @return area under the ROC curve, estimated by trapezoidal approx.
   */
  public static double computeAUC(List<Pair<Boolean, Double>> inPoints, TieBreaking what) {
    if(inPoints.size() < 2) {
      throw new UnsupportedOperationException("Cannot compute AUC without at least two points.");
    }
    //List<Pair<Boolean, Double>> points = new ArrayList<>(inPoints);
    List<PredTruth> points = ListFns.map(inPoints, PredTruth::new);
    // order by prediction confidence:
    points.sort(getComparator(what));

    double[] true_pos_rate = new double[points.size()];
    double[] false_pos_rate = new double[points.size()];

    int total_true_pos = 0;
    int total_false_pos = 0;
    for (PredTruth point : points) {
      if(point.truth)
        total_true_pos++;
      else
        total_false_pos++;
    }


    // walk the ranked list and build graph:
    int true_pos = 0;
    int false_pos = 0;
    for (int i = 0; i < points.size(); i++) {

      if(points.get(i).truth) {
        true_pos++;
      } else {
        false_pos++;
      }
      double N = i+1;
      true_pos_rate[i] = true_pos / (double) total_true_pos;
      false_pos_rate[i] = false_pos / (double) total_false_pos;
    }

    // estimate area under the curve using the trapezoidal rule, as SciKitLearn does via np.trapz
    final double[] y = true_pos_rate;
    final double[] x = false_pos_rate;

    int lim = x.length-1;
    double sum = 0;
    for (int i = 0; i < lim; i++) {
      double dx = x[i+1]-x[i];
      double midy = (y[i+1]+y[i]); // div 2 hoisted to end
      sum += dx*midy;
    }
    return sum / 2.0;
  }

  public static double maximizeAccuracy(List<Pair<Boolean, Double>> inPoints) {
    return maximizeAccuracy(inPoints, TieBreaking.WORST_CASE);
  }
  /** Returns a cutoff that maximizes accuracy. */
  public static double maximizeAccuracy(List<Pair<Boolean, Double>> inPoints, TieBreaking kind) {
    List<PredTruth> points = ListFns.map(inPoints, PredTruth::new);
    // order by prediction confidence:
    points.sort(getComparator(kind));

    double[] acc = new double[points.size()];

    for (int i = 0; i < points.size(); i++) {
      BinaryClassifierInfo info = new BinaryClassifierInfo();
      for (int j = 0; j < points.size(); j++) {
        if(j <= i) {
          info.update(true, points.get(j).truth);
        } else {
          info.update(false, points.get(j).truth);
        }
      }
      acc[i] = info.getAccuracy();
    }

    int best_index = 0;
    double maxAcc = 0;
    for (int i = 0; i < acc.length; i++) {
      if(acc[i] > maxAcc) {
        maxAcc = acc[i];
        best_index = i;
      }
    }

    //System.out.println("Best F1: "+maxF1+" at rank="+(best_index+1));

    int lhs = best_index;
    int rhs = best_index+1;
    if(rhs >= points.size()) {
      double leftleft = points.get(lhs-1).prediction;
      double here = points.get(lhs).prediction;
      double diff = leftleft - here;
      return here - diff;
    } else {
      return (points.get(lhs).prediction + points.get(rhs).prediction) / 2.0;
    }
  }

  public static double maximizeF1(List<Pair<Boolean, Double>> inPoints) {
    return maximizeFScore(inPoints, 1.0);
  }
  public static double maximizeFScore(List<Pair<Boolean, Double>> inPoints, double beta) {
    List<PredTruth> points = ListFns.map(inPoints, PredTruth::new);
    // order by prediction confidence:
    points.sort(getComparator(TieBreaking.WORST_CASE));

    int total_true_pos = 0;
    for (PredTruth point : points) {
      if(point.truth)
        total_true_pos++;
    }

    double[] f1 = new double[points.size()];
    double betaSq = beta * beta;

    int true_pos = 0;
    for (int i = 0; i < points.size(); i++) {
      if (points.get(i).truth) {
        true_pos++;
      }
      double k = i + 1;
      if(true_pos == 0) {
        f1[i] = 0;
      } else {
        double prec_k = true_pos / k;
        double recall_k = true_pos / (double) total_true_pos;
        f1[i] = (1.0 + betaSq) * (prec_k * recall_k) / ((betaSq * prec_k) + recall_k);
      }
    }

    int best_index = 0;
    double maxF1 = 0;
    for (int i = 0; i < f1.length; i++) {
      if(f1[i] > maxF1) {
        maxF1 = f1[i];
        best_index = i;
      }
    }

    //System.out.println("Best F1: "+maxF1+" at rank="+(best_index+1));

    int lhs = best_index;
    int rhs = best_index+1;
    if(rhs >= points.size()) {
      double leftleft = points.get(lhs-1).prediction;
      double here = points.get(lhs).prediction;
      double diff = leftleft - here;
      return here - diff;
    } else {
      return (points.get(lhs).prediction + points.get(rhs).prediction) / 2.0;
    }
  }

  public static double computePrec(List<Pair<Boolean, Double>> data, int cutoff) {
    List<PredTruth> points = ListFns.map(data, PredTruth::new);
    // order by prediction confidence:
    points.sort(getComparator(TieBreaking.WORST_CASE));
    int N = Math.min(cutoff, data.size());
    int correct = 0;
    for (int i = 0; i < N; i++) {
      if(data.get(i).left) {
        correct++;
      }
    }
    if(correct == 0) return 0;
    return correct / (double) N;
  }

  public static double computeAP(List<Pair<Boolean, Double>> data) {
    return computeAP(data, TieBreaking.WORST_CASE);
  }
  public static double computeAP(List<Pair<Boolean, Double>> data, TieBreaking kind) {
    int numRelevant = 0;
    for (Pair<Boolean, Double> booleanDoublePair : data) {
      if(booleanDoublePair.left) numRelevant++;
    }
    return computeAP(data, numRelevant, kind);
  }

  public static double computeAP(List<Pair<Boolean, Double>> data, int numRelevant) {
    return computeAP(data, numRelevant, TieBreaking.WORST_CASE);
  }

  public static double computeAP(List<Pair<Boolean, Double>> data, int numRelevant,TieBreaking kind) {
    // if there are no relevant documents,
    // the average is artificially defined as zero, to mimic trec_eval
    // Really, the output is NaN, or the query should be ignored.
    if(numRelevant == 0) return 0;

    List<PredTruth> points = ListFns.map(data, PredTruth::new);
    // order by prediction confidence:
    points.sort(getComparator(kind));

    double sumPrecision = 0;
    int recallPointCount = 0;

    for (int i = 0; i < points.size(); i++) {
      PredTruth point = points.get(i);
      if(point.truth) {
        double rank = i + 1;
        recallPointCount++;
        sumPrecision += recallPointCount / rank;
      }
    }

    return sumPrecision / numRelevant;
  }

  public static class PredTruth {
    boolean truth;
    double prediction;

    public PredTruth(Pair<Boolean, Double> input) {
      this(input.left, input.right);
    }
    public PredTruth(boolean truth, double prediction) {
      this.truth = truth;
      this.prediction = prediction;
    }

    public static Comparator<PredTruth> scoreTiesWorstCase = (o1, o2) -> {
      // highest scores first:
      int score = -Double.compare(o1.prediction, o2.prediction);
      if(score != 0) return score;
      // false, then true:
      return Boolean.compare(o1.truth, o2.truth);
    };

    public static Comparator<PredTruth> scoreTiesBestCase = (o1, o2) -> {
      // highest scores first:
      int score = -Double.compare(o1.prediction, o2.prediction);
      if(score != 0) return score;
      // true, then false:
      return -Boolean.compare(o1.truth, o2.truth);
    };
  }
}
