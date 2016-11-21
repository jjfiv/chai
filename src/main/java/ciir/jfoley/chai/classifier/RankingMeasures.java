package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley
 */
public class RankingMeasures {
  /**
   * Note still a lingering issue w.r.t. how to break ties. This is okay because in practice, w.r.t. to classification instances, it is really unlikely that we will find ties.
   * @param inPoints list of (true judgment, score) pairs
   * @return area under the ROC curve, estimated by trapezoidal approx.
   */
  public static double computeAUC(List<Pair<Boolean, Double>> inPoints) {
    if(inPoints.size() < 2) {
      throw new UnsupportedOperationException("Cannot compute AUC without at least two points.");
    }
    List<Pair<Boolean, Double>> points = new ArrayList<>(inPoints);
    // order by prediction confidence:
    points.sort((lhs, rhs) -> -Double.compare(lhs.right, rhs.right));

    double[] true_pos_rate = new double[points.size()];
    double[] false_pos_rate = new double[points.size()];

    int total_true_pos = 0;
    int total_false_pos = 0;
    for (Pair<Boolean, Double> point : points) {
      if(point.left)
        total_true_pos++;
      else
        total_false_pos++;
    }


    // walk the ranked list and build graph:
    int true_pos = 0;
    int false_pos = 0;
    for (int i = 0; i < points.size(); i++) {

      if(points.get(i).left) {
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

  /** Returns a cutoff that maximizes accuracy. */
  public static double maximizeAccuracy(List<Pair<Boolean, Double>> inPoints) {
    List<Pair<Boolean, Double>> points = new ArrayList<>(inPoints);
    // order by prediction confidence:
    points.sort((lhs, rhs) -> -Double.compare(lhs.right, rhs.right));

    double[] acc = new double[points.size()];

    for (int i = 0; i < points.size(); i++) {
      BinaryClassifierInfo info = new BinaryClassifierInfo();
      for (int j = 0; j < points.size(); j++) {
        if(j <= i) {
          info.update(true, points.get(j).left);
        } else {
          info.update(false, points.get(j).left);
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
      double leftleft = points.get(lhs-1).right;
      double here = points.get(lhs).right;
      double diff = leftleft - here;
      return here - diff;
    } else {
      return (points.get(lhs).right + points.get(rhs).right) / 2.0;
    }
  }

  public static double maximizeF1(List<Pair<Boolean, Double>> inPoints) {
    return maximizeFScore(inPoints, 1.0);
  }
  public static double maximizeFScore(List<Pair<Boolean, Double>> inPoints, double beta) {
    List<Pair<Boolean, Double>> points = new ArrayList<>(inPoints);
    // order by prediction confidence:
    points.sort((lhs, rhs) -> -Double.compare(lhs.right, rhs.right));

    int total_true_pos = 0;
    for (Pair<Boolean, Double> point : points) {
      if(point.left)
        total_true_pos++;
    }

    double[] f1 = new double[points.size()];
    double betaSq = beta * beta;

    int true_pos = 0;
    for (int i = 0; i < points.size(); i++) {
      if (points.get(i).left) {
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
      double leftleft = points.get(lhs-1).right;
      double here = points.get(lhs).right;
      double diff = leftleft - here;
      return here - diff;
    } else {
      return (points.get(lhs).right + points.get(rhs).right) / 2.0;
    }
  }

  public static double computePrec(List<Pair<Boolean, Double>> data, int cutoff) {
    List<Pair<Boolean, Double>> points = new ArrayList<>(data);
    // order by prediction confidence:
    points.sort((lhs, rhs) -> -Double.compare(lhs.right, rhs.right));
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
    int numRelevant = 0;
    for (Pair<Boolean, Double> booleanDoublePair : data) {
      if(booleanDoublePair.left) numRelevant++;
    }
    return computeAP(data, numRelevant);
  }
  public static double computeAP(List<Pair<Boolean, Double>> data, int numRelevant) {
    // if there are no relevant documents,
    // the average is artificially defined as zero, to mimic trec_eval
    // Really, the output is NaN, or the query should be ignored.
    if(numRelevant == 0) return 0;

    List<Pair<Boolean, Double>> points = new ArrayList<>(data);
    // order by prediction confidence:
    points.sort((lhs, rhs) -> -Double.compare(lhs.right, rhs.right));

    double sumPrecision = 0;
    int recallPointCount = 0;

    for (int i = 0; i < points.size(); i++) {
      Pair<Boolean, Double> point = points.get(i);
      if(point.left) {
        double rank = i + 1;
        recallPointCount++;
        sumPrecision += recallPointCount / rank;
      }
    }

    return sumPrecision / numRelevant;
  }
}
