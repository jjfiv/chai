package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jfoley
 */
public class PerceptronTest {

  @Test
  public void testSimpleDataset() {
    List<Pair<Integer, float[]>> dataset = Arrays.asList(
        Pair.of(1, new float[] {1, 0}),
        Pair.of(1, new float[] {1, 2}),
        Pair.of(1, new float[] {1, 0}),
        Pair.of(1, new float[] {1, 3}),
        Pair.of(1, new float[] {1, 0}),
        Pair.of(-1, new float[] {-1, 1}),
        Pair.of(-1, new float[] {-1, 2}),
        Pair.of(-1, new float[] {-1, 0}),
        Pair.of(-1, new float[] {-1, 4}),
        Pair.of(-1, new float[] {-1, 0})
    );

    int numDim = 2;
    int maxIters = 100;
    Perceptron perceptron = new Perceptron(numDim, maxIters);
    perceptron.setRandomSeed(13);
    BinaryClassifierInfo train = perceptron.train(dataset);

    // easy data, should converge:
    assertEquals(1.0, train.getAccuracy(), 0.0001);
    // easy data, should converge fast:
    assertTrue(train.numIterations < 10);
  }

  @Test
  public void testUnbalancedDataset() {
    List<Pair<Integer, float[]>> dataset = Arrays.asList(
        Pair.of(1, new float[] {1, 0}),
        Pair.of(1, new float[] {1, 2}),
        Pair.of(1, new float[] {1, 0}),
        Pair.of(1, new float[] {1, 3}),
        Pair.of(1, new float[] {1, 0}),
        Pair.of(-1, new float[] {-1, 0})
    );

    int numDim = 2;
    int maxIters = 100;
    Perceptron perceptron = new Perceptron(numDim, maxIters);
    perceptron.setRandomSeed(13);
    BinaryClassifierInfo train = perceptron.train(dataset);

    // easy data, should converge:
    assertEquals(1.0, train.getAccuracy(), 0.0001);
    // easy data, should converge fast:
    assertTrue(train.numIterations < 10);
  }

  @Test
  public void testHardDataset() {
    List<Pair<Integer, float[]>> dataset = Arrays.asList(
        Pair.of(1, new float[] {1, 1}),
        Pair.of(1, new float[] {1, 1}),
        Pair.of(1, new float[] {1, 1}),
        Pair.of(1, new float[] {1, 1}),
        Pair.of(-1, new float[] {1, 1})
    );

    int numDim = 2;
    int maxIters = 100;
    Perceptron perceptron = new Perceptron(numDim, maxIters);
    perceptron.setRandomSeed(14);
    BinaryClassifierInfo train = perceptron.train(dataset);

    // should get 4/5 right, can't distinguish the last one.
    assertEquals(maxIters, train.numIterations);
    assertEquals(0.8, train.getAccuracy(), 0.001);
    assertEquals(1.0, train.getPositiveRecall(), 0.001);
    assertEquals(0.8, train.getPositivePrecision(), 0.001);
    assertEquals(0.0, train.getNegativeRecall(), 0.001);
    assertEquals(0.0, train.getNegativePrecision(), 0.001);
  }

}