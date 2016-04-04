package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jfoley
 */
public abstract class Classifier {
  /** Source of randomness for classifiers. {@see Classifier.setRandomSeed} */
  protected transient Random random = new Random();

  public void setRandomSeed(int x) {
    this.random = new Random(x);
  }

  /** Return training accuracy, store weights within this classifier object. */
  public abstract BinaryClassifierInfo train(List<Pair<Integer, float[]>> data);
  /** Predict the class of a given data point. */
  public abstract int predict(float[] v);
  /** Reset the weights in this classifier so it can be retrained. */
  public abstract void reset();

  /** Balance a list of training data pseudo-randomly. */
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

  /** Balance a training set before submitting it to this classifier. */
  public BinaryClassifierInfo trainBalanced(List<Pair<Integer, float[]>> train, Random rand) {
    // train on balanced set:
    List<Pair<Integer, float[]>> balancedTraining = balance(train, rand);
    BinaryClassifierInfo trained = train(balancedTraining);

    // return stats on original, unbalanced set:
    BinaryClassifierInfo validateUB = predict(train);
    validateUB.numIterations = trained.numIterations;
    return validateUB;
  }

  /** Predict on a large set of data, typically for validation or evaluation. */
  public BinaryClassifierInfo predict(List<Pair<Integer, float[]>> validateData) {
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

}
