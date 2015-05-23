package ciir.jfoley.chai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IntMathTest {

  @Test
  public void testNearestPowerOfTwo() throws Exception {
    assertEquals(1, IntMath.nearestPowerOfTwo(0));
    assertEquals(2, IntMath.nearestPowerOfTwo(1));
    assertEquals(4, IntMath.nearestPowerOfTwo(2));
    assertEquals(8, IntMath.nearestPowerOfTwo(7));
    assertEquals(16, IntMath.nearestPowerOfTwo(8));
    assertEquals(16, IntMath.nearestPowerOfTwo(9));
    assertEquals(32, IntMath.nearestPowerOfTwo(16));
  }

  @Test
  public void testFromLong() {
    try {
      assertEquals(0, IntMath.fromLong(1L << 45));
      fail("This is super un-possible.");
    } catch (NumberFormatException nfe) {

    }
    // assertEquals(1<<31, IntMath.fromLong(1L << 31)); // This one wraps negative!
    assertTrue(1L << 31 > 0);
    assertTrue(1 << 31 < 0);

    for (int i = 0; i < 31; i++) {
      assertEquals(1<<i, IntMath.fromLong(1L << i));
    }
  }
}