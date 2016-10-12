package ciir.jfoley.chai.time;

import ciir.jfoley.chai.collections.list.IntList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jfoley
 */
public class DebouncerTest {
  @Test
  public void testPrinting() {
    assertEquals("50/100 items,  0.8 items/s 60.0 seconds left; 60.0 seconds spent, 50.0% complete.", new Debouncer.RateEstimate(60*1000, 50, 100).toString());
  }

  @Test
  public void testDebouncing() {
    Debouncer msg = new Debouncer(5000);
    IntList items = new IntList();
    for (int i = 0; i < 10000; i++) {
      if(msg.ready()) {
        items.push(i);
      }
    }
    // only first item with large debounce:
    assertEquals(IntList.from(0), items);
    Debouncer.RateEstimate estimate = msg.estimate(10000, 10000);
    assertTrue(estimate.itemsPerSecond() > 10000);
    assertEquals(0.0, estimate.secondsLeft(), 0.001);
    assertEquals(0.0, estimate.secondsInvested(), 0.1);
    assertEquals(100.0, estimate.percentComplete(), 0.001);
  }

}