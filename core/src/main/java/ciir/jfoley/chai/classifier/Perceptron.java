package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * @author jfoley
 */
public class Perceptron implements Serializable {
  public Random random = new Random();

  public static List<Pair<Integer, float[]>> balance(List<Pair<Integer, float[]>> input, Random rand) {
    List<Pair<Integer, float[]>> neg = new ArrayList<>();
    List<Pair<Integer, float[]>> pos = new ArrayList<>();

    for (Pair<Integer, float[]> instance : input) {
      if(instance.left < 0) {
        neg.add(instance);
      } else {
        pos.add(instance);
      }
    }
    if(neg.size() == pos.size()) return input;

    List<Pair<Integer, float[]>> smaller = neg.size() < pos.size() ? neg : pos;
    List<Pair<Integer, float[]>> larger = neg.size() < pos.size() ? pos : neg;

    List<Pair<Integer, float[]>> clones = new ArrayList<>();
    for (int i = smaller.size(); i < larger.size(); i++) {
      clones.add(smaller.get(rand.nextInt(smaller.size())));
    }
    clones.addAll(smaller);
    clones.addAll(larger);
    return clones;
  }
  public static List<Pair<Integer, float[]>> balance(List<Pair<Integer, float[]>> input) {
    return balance(input, new Random());
  }

  private static final long serialVersionUID = 0x71f941717ddd39b1L;
  final int ND;
  float w[];
  float trainingAccuracy;
  int trainingNumIters;


  public Perceptron(int nd) {
    ND = nd;
    w = new float[ND + 1];
    reset();
  }

  public void setRandomSeed(int x) {
    this.random = new Random(x);
  }

  public void reset() {
    Arrays.fill(w, 0.0f);
    w[ND] = 1;
    trainingAccuracy = Float.NaN;
    trainingNumIters = 0;
  }

  public BinaryClassifierInfo train(List<Pair<Integer, float[]>> data, int numIterations) {
    Collections.shuffle(data, this.random);

    assert (ND == data.get(0).right.length);

    int total = data.size();
    int correct = 0;
    int updates = 0;
    boolean changed = false;
    int numIters;

    // average perceptron builds a temporary vector and adds it to w occasionally
    float[] tmpW = new float[ND + 1];
    tmpW[ND] = 1;
    // how long this w has survived:
    int wLife = 0;

    long startTime = System.currentTimeMillis();
    for (numIters = 0; numIters < numIterations; numIters++) {
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
          updates++;
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

    BinaryClassifierInfo trainedInfo = validate(data);
    trainedInfo.numIterations = numIters;
    trainedInfo.time = endTime - startTime;
    return trainedInfo;
  }

  /**
   * @param fv input feature vector.
   * @return 1 if true, -1 if false class.
   */
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

  public BinaryClassifierInfo validate(List<Pair<Integer, float[]>> validateData) {
    BinaryClassifierInfo out = new BinaryClassifierInfo();

    long startTime = System.currentTimeMillis();
    for (Pair<Integer, float[]> kv : validateData) {
      int label = kv.getKey();
      float[] fv = kv.getValue();
      int plabel = predict(fv);
      out.update(plabel >= 0, label >= 0);
    }
    long endTime = System.currentTimeMillis();
    out.time = endTime - startTime;

    return out;
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
}
