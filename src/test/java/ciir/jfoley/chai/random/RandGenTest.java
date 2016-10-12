package ciir.jfoley.chai.random;

import ciir.jfoley.chai.math.FullStats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class RandGenTest {

  // This test is slightly non-deterministic, but it should be fine:
  @Test
  public void testSplitMix() {
    SplitMix64 rng = new SplitMix64();

    FullStats stats = new FullStats();
    for (int i = 0; i < 30000; i++) {
      stats.push(rng.nextInt(101));
    }

    assertEquals(50.0, stats.getMedian(), 1.0);
    assertEquals(30000, stats.getN());
    assertEquals(0, stats.getPercentile(0), 1.0);
    assertEquals(stats.getMedian(), stats.getPercentile(50), 0.01);
    assertEquals(100, stats.getPercentile(100), 1.0);
  }

  // This test is slightly non-deterministic, but it should be fine:
  @Test
  public void testXoroshiro() {
    RandGen rng = new Xoroshiro128Plus();

    FullStats stats = new FullStats();
    for (int i = 0; i < 30000; i++) {
      stats.push(rng.nextInt(101));
    }

    assertEquals(50.0, stats.getMedian(), 1.0);
    assertEquals(30000, stats.getN());
    assertEquals(0, stats.getPercentile(0), 1.0);
    assertEquals(stats.getMedian(), stats.getPercentile(50), 0.01);
    assertEquals(100, stats.getPercentile(100), 1.0);
  }

}