package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.IntRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley.
 */
public class QuickSortTest {

  @Test
  public void testSort() throws Exception {
    List<Integer> existing = IntRange.exclusive(0, 100);
    List<Integer> data = new ArrayList<>(existing);

    Collections.shuffle(data);
    //assertNotEquals(existing, data); // Technically this could fail.

    QuickSort.sort(data);
    assertEquals(existing, data);
  }
}