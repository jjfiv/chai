package ciir.jfoley.chai.collections;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class IntRangeTest {

  @Test
  public void testInclusive() throws Exception {
    IntRange digits = IntRange.inclusive(0,9);
    assertEquals(Arrays.asList(0,1,2,3,4,5,6,7,8,9), digits);
    assertEquals(9, digits.inclusiveEnd());
    assertEquals(10, digits.exclusiveEnd());

    assertEquals(Arrays.asList(7,8,9), IntRange.inclusive(7,9));
  }

  @Test
  public void testExclusive() throws Exception {
    IntRange digits = IntRange.exclusive(0, 4);
    assertEquals(Arrays.asList(0,1,2,3), digits);
    assertEquals(Arrays.asList(2,3), IntRange.exclusive(2, 4));
  }

  @Test
  public void intersects() {
    IntRange a = IntRange.exclusive(0, 4);
    IntRange b = IntRange.exclusive(2, 6);
    IntRange c = IntRange.exclusive(4, 8);
    IntRange d = IntRange.exclusive(6, 10);
    assertFalse(a.intersects(c));
    assertFalse(c.intersects(a));

    assertFalse(a.intersects(d));
    assertFalse(d.intersects(a));

    assertFalse(b.intersects(d));
    assertFalse(d.intersects(b));

    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));

    assertTrue(b.intersects(c));
    assertTrue(c.intersects(b));

    assertTrue(c.intersects(d));
    assertTrue(d.intersects(c));
  }
}