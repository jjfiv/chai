package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class DoubleListTest {
  @Test
  public void simpleTest() {
    DoubleList list = new DoubleList();
    list.push(1.0);
    list.push(2.0);
    list.push(3);

    assertEquals(3, list.fill);
    assertEquals(1.0, list.getQuick(0), 0.0000001);
    assertEquals(2.0, list.getQuick(1), 0.0000001);
    assertEquals(3.0, list.getQuick(2), 0.0000001);
    assertEquals(Arrays.asList(1.0, 2.0, 3.0), (List<Double>) list);
    assertEquals(new DoubleList(Arrays.asList(1.0,2.0,3.0)), list);

    assertEquals(6.0, list.sum(), 0.000001);
    assertEquals(2.0, list.mean(), 0.000001);

    assertEquals(1.0, list.unsafeArray()[0], 0.0000001);
    assertEquals(2.0, list.unsafeArray()[1], 0.0000001);
    assertEquals(3.0, list.unsafeArray()[2], 0.0000001);
  }

}