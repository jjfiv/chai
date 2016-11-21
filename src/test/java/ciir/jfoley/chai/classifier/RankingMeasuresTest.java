package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author jfoley
 */
public class RankingMeasuresTest {
  static List<Pair<Boolean, Double>> emptyList = Collections.emptyList();
  @Test
  public void testPrec() {
    assertEquals(0.0, RankingMeasures.computePrec(emptyList, 10), 0.00001);
    assertEquals(0.0, RankingMeasures.computePrec(emptyList, 5), 0.00001);
  }

  @Test
  public void testAUC() {
    List<Pair<Boolean, Double>> predictions = Arrays.asList(
        Pair.of(true, 5.0),
        Pair.of(true, 4.5),
        Pair.of(false, 4.0),
        Pair.of(true, 3.0),
        Pair.of(true, 3.0),
        Pair.of(true, 2.0),
        Pair.of(false, 1.0),
        Pair.of(false, 1.0),
        Pair.of(false, 0.0),
        Pair.of(false, 0.0),
        Pair.of(false, -1.0)
    );

    assertEquals(0.9, RankingMeasures.computeAUC(predictions), 0.001);
    try {
      assertEquals(0.0, RankingMeasures.computeAUC(emptyList), 0.001);
      fail("AUC should break with empty input.");
    } catch (UnsupportedOperationException uoe) {
      assertNotNull(uoe);
    }

    predictions = Arrays.asList(
        Pair.of(true, 5.0),
        Pair.of(true, 4.5),
        Pair.of(false, 4.0),
        Pair.of(true, 2.0),
        Pair.of(true, 1.1),
        Pair.of(true, 1.1),
        Pair.of(false, 1.0),
        Pair.of(false, 1.0),
        Pair.of(true, 0.0),
        Pair.of(true, 0.0),
        Pair.of(false, -1.0)
    );

    assertEquals(0.6785, RankingMeasures.computeAUC(predictions), 0.0001);
  }
}