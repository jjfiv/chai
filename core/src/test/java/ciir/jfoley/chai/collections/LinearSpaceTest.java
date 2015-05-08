package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.util.ArrayFns;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author jfoley.
 */
public class LinearSpaceTest {

  @Test
  public void testOf() throws Exception {
    assertArrayEquals(
        new double[]{0.0, 1.0},
        ArrayFns.fromList(LinearSpace.of(0.0, 1.0, 2)), 0.001);

    assertArrayEquals(
        new double[]{0.0, 0.25, 0.5, 0.75, 1.0},
        ArrayFns.fromList(LinearSpace.of(0.0, 1.0, 5)),
        0.001);
  }
}