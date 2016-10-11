package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Note that when testing, order will matter.
 * @author jfoley
 */
public class AUCTest {

  @Test
  public void test() {
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

    assertEquals(0.9, AUC.compute(predictions), 0.001);

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

    assertEquals(0.6785, AUC.compute(predictions), 0.0001);
  }

}