package ciir.jfoley.chai.collections.iters;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class GroupByIteratorTest {

  @Test
  public void testLazyGroupBy() {
    List<Integer> data = Arrays.asList(
        3,3,3,
        2,2,
        1,
        4,4,4,4,
        6,6,6,6,6,6);

    for (List<Integer> xs : new OneShotIterable<>(new GroupByIterator<>(data.iterator()))) {
      int value = xs.get(0);
      assertEquals(value, xs.size());
    }
  }

}