package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley
 */
public class AUC {
  /**
   * Note still a lingering issue w.r.t. how to break ties. This is okay because in practice, w.r.t. to classification instances, it is really unlikely that we will find ties.
   * @param inPoints list of (true judgment, score) pairs
   * @return area under the ROC curve, estimated by trapezoidal approx.
   */
  public static double compute(List<Pair<Boolean, Double>> inPoints) {
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

  public static void main(String[] args) {

  }
}
