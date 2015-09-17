package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class FixedSlidingWindowTest {

  @Test
  public void testSimple() {
    FixedSlidingWindow<Integer> data = new FixedSlidingWindow<>(5);
    data.add(1);
    data.add(2);

    assertEquals(Arrays.asList(1, 2), data);
    data.add(3);
    data.add(4);
    data.add(5);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5), data);

    data.add(6);
    assertEquals(Arrays.asList(6, 2, 3, 4, 5), data.data);
    assertEquals(Arrays.asList(2, 3, 4, 5, 6), data);

    assertEquals(2, data.replace(7).intValue());
    assertEquals(Arrays.asList(3, 4, 5, 6, 7), data);
    assertEquals(Arrays.asList(3, 4, 5, 6, 7).hashCode(), data.hashCode());
  }

  @Test
  public void testPrimVersion() {
    CircularIntBuffer data = new CircularIntBuffer(5);
    data.add(1);
    data.add(2);

    assertEquals(Arrays.asList(1, 2), (List<Integer>) data);
    data.add(3);
    data.add(4);
    data.add(5);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5), (List<Integer>) data);

    data.add(6);
    assertArrayEquals(new int[]{6, 2, 3, 4, 5}, data.data);
    assertEquals(Arrays.asList(2, 3, 4, 5, 6), (List<Integer>) data);

    assertEquals(2, data.replace(7));
    assertEquals(Arrays.asList(3, 4, 5, 6, 7), (List<Integer>) data);
    assertEquals(Arrays.asList(3, 4, 5, 6, 7).hashCode(), data.hashCode());


  }
}