package ciir.jfoley.chai.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jfoley
 */
public class StreamingStatsTest {

  private static final double delta = 1e-5;

  @Test
  public void testStats() {
    double[] stupidData =new double[] { 0,0,1,1 };
    assertEquals(0.5, Stats.mean(stupidData), delta);
    assertEquals(0.33333, Stats.variance(stupidData), delta);
    assertEquals(0.57735, Stats.standardDeviation(stupidData), delta);

    StreamingStats ss = new StreamingStats();
    for (double v : stupidData) {
      ss.push(v);
    }
    assertEquals(0.5, ss.getMean(), delta);
    assertEquals(0.33333, ss.getVariance(), delta);
    assertEquals(1.0, ss.getMax(), delta);
    assertEquals(0.0, ss.getMin(), delta);
    assertEquals(0.57735, ss.getStandardDeviation(), delta);
    assertEquals(ss.getTotal() / ss.getCount(), ss.getMean(), delta);
  }

  @Test
  public void testStatsRand() {
    Random rand = new Random();
    double[] data = new double[1000];
    StreamingStats ss = new StreamingStats();
    for (int i = 0; i < 1000; i++) {
      data[i] = rand.nextDouble();
      ss.push(data[i]);
    }

    assertEquals(Stats.mean(data), ss.getMean(), delta);
    assertEquals(Stats.variance(data), ss.getVariance(), delta);
  }


  /**
   * This test might be flaky...
   */
  @Test
  public void testStatsSumRand() {
    Random rand = new Random();
    double[] data = new double[1000];
    StreamingStats ss = new StreamingStats();
    StreamingStats ss1 = new StreamingStats();
    StreamingStats ss2 = new StreamingStats();
    for (int i = 0; i < 1000; i++) {
      data[i] = rand.nextDouble();
      ss.push(data[i]);
      if(i < 500) {
        ss1.push(data[i]);
      } else {
        ss2.push(data[i]);
      }
    }

    ss1.add(ss2);

    assertEquals(Stats.mean(data), ss.getMean(), delta);
    assertEquals(Stats.variance(data), ss.getVariance(), delta);

    assertEquals(ss.getMean(), ss1.getMean(), delta);
    assertEquals(ss.getStandardDeviation(), ss1.getStandardDeviation(), 0.001);
    assertEquals(ss.getVariance(), ss1.getVariance(), 0.01);
    assertEquals(ss.getMax(), ss1.getMax(), delta);
    assertEquals(ss.getMin(), ss1.getMin(), delta);
    assertEquals(ss.getTotal(), ss1.getTotal(), delta);
    assertEquals(ss.getCount(), ss1.getCount(), delta);
  }

  @Test
  public void testMaxOfNegatives() {
    Random rand = new Random();
    StreamingStats stats = new StreamingStats();
    for (int i = 0; i < 1000; i++) {
      double x = rand.nextGaussian() - 8;
      stats.push(x);
    }

    assertTrue(stats.getMax() < 0);
    assertTrue(stats.getMean() < 0);
    assertTrue(stats.getMin() < 0);
  }
}