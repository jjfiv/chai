package ciir.jfoley.chai.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jfoley
 */
public class StreamingStatsTest {

  @Test
  public void testStats() {
    double[] stupidData =new double[] { 0,0,1,1 };
    assertEquals(0.5, Stats.mean(stupidData), 0.00001);
    assertEquals(0.33333, Stats.variance(stupidData), 0.00001);
    assertEquals(0.57735, Stats.standardDeviation(stupidData), 0.00001);

    StreamingStats ss = new StreamingStats();
    for (double v : stupidData) {
      ss.push(v);
    }
    assertEquals(0.5, ss.getMean(), 0.00001);
    assertEquals(0.33333, ss.getVariance(), 0.00001);
    assertEquals(1.0, ss.getMax(), 0.00001);
    assertEquals(0.0, ss.getMin(), 0.00001);
    assertEquals(0.57735, ss.getStandardDeviation(), 0.00001);
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

    assertEquals(Stats.mean(data), ss.getMean(), 1e-10);
    assertEquals(Stats.variance(data), ss.getVariance(), 1e-10);
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