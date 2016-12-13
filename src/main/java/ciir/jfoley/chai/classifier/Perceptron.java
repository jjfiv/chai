package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * Slight misnomer, actually an Averaged-Perceptron.
 * @author jfoley
 */
public class Perceptron extends Classifier implements Serializable {
  private final int maxIterations;
  private static final long serialVersionUID = 0x71f941717ddd39b1L;

  final int ND;
  float w[];

  public Perceptron(int nd, int maxIterations) {
    this.maxIterations = maxIterations;
    ND = nd;
    w = new float[ND + 1];
    reset();
  }

  @Override
  public void reset() {
    Arrays.fill(w, 0.0f);
    w[ND] = 1;
  }

  /** For online training, classify and update if necessary. */
  public void trainSingleInstance(int label, float[] fv) {
    int predicted = predict(ND, w, fv);
    if (predicted != label) {
      // update:
      for (int i = 0; i < ND; i++) {
        w[i] += label * fv[i];
      }
      w[w.length - 1] += label;
    }
  }

  @Override
  public BinaryClassifierInfo train(List<Pair<Integer, float[]>> data) {
    Collections.shuffle(data, this.random);

    assert (ND == data.get(0).right.length);

    int total = data.size();
    int correct = 0;
    boolean changed = false;
    int numIters;

    // average perceptron builds a temporary vector and adds it to w occasionally
    float[] tmpW = new float[ND + 1];
    for (int i = 0; i < ND; i++) {
      tmpW[i] = (float) this.random.nextGaussian();
    }
    tmpW[ND] = 1;
    // how long this w has survived:
    int wLife = 0;

    long startTime = System.currentTimeMillis();
    for (numIters = 0; numIters < maxIterations; numIters++) {
      correct = 0;
      changed = false;
      for (Pair<Integer, float[]> labeledData : data) {
        int label = labeledData.left;
        float[] fv = labeledData.right;

        int predicted = predict(ND, tmpW, fv);

        if (predicted != label) {
          // add in tmp vector, weighted by number of iterations:
          for (int i = 0; i <= ND; i++) {
            w[i] += wLife * tmpW[i];
          }

          // update:
          for (int i = 0; i < ND; i++) {
            tmpW[i] += label * fv[i];
          }
          w[w.length - 1] += label;

          changed = true;
          wLife = 0;
        } else {
          wLife++;
          correct++;
        }
      } // data-loop

      //System.out.printf("Iteration: %d, correct:%d/%d, changed=%s\n", numIters, correct, total, changed);
      // skip iterations if we're somehow perfect:
      if (!changed && correct == total) {
        numIters+=1;
        break;
      }
    } // iter-loop
    long endTime = System.currentTimeMillis();

    // add in tmp vector, weighted by number of iterations:
    for (int i = 0; i <= ND; i++) {
      w[i] += wLife * tmpW[i];
    }

    BinaryClassifierInfo trainedInfo = predict(data);
    trainedInfo.numIterations = numIters;
    trainedInfo.time = endTime - startTime;
    return trainedInfo;
  }

  /**
   * @param fv input feature vector.
   * @return 1 if true, -1 if false class.
   */
  @Override
  public final int predict(float[] fv) {
    return predict(ND, w, fv);
  }

  static int predict(int ND, float[] w, float[] fv) {
    double dotP = 0.0;
    for (int i = 0; i < ND; i++) {
      dotP += w[i] * fv[i];
    }
    dotP += w[w.length - 1]; // * 1.0 if we extended all the features.
    return dotP >= 0 ? 1 : -1;
  }

  public String toString() {
    return "Perceptron("+Arrays.toString(w)+")";
  }

  public float dotP(float[] fv) {
    float dotP = 0f;
    for (int i = 0; i < ND; i++) {
      dotP += w[i] * fv[i];
    }
    return dotP;
  }
  public double cosineSimilarity(float[] fv) {
    double dotP = 0.0;
    double lhs = 0.0;
    double rhs = 0.0;
    for (int i = 0; i < ND; i++) {
      dotP += w[i] * fv[i];
      lhs += w[i] * w[i];
      rhs += fv[i] * fv[i];
    }
    return dotP / (lhs * rhs);
  }

  public void normalize() {
    float total = 0f;
    for (int i = 0; i < w.length; i++) {
      total += w[i];
    }
    if(total == 0) return;
    for (int i = 0; i < w.length; i++) {
      w[i] /= total;
    }
  }

  public float[] copyWeights() {
    return Arrays.copyOf(w, w.length);
  }
}
