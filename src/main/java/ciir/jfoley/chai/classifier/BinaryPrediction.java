package ciir.jfoley.chai.classifier;

/**
 * @author jfoley
 */
public class BinaryPrediction {
  final boolean truth;
  final boolean predicted;

  public BinaryPrediction(boolean truth, boolean predicted) {
    this.truth = truth;
    this.predicted = predicted;
  }

  public static BinaryPrediction fromPerceptron(boolean label, int pred) {
    return new BinaryPrediction(label, pred == 1);
  }
}
