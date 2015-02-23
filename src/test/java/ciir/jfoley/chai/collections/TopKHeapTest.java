package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.util.Comparing;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TopKHeapTest {

  @Test
  public void testGetSorted() throws Exception {
    TopKHeap<Integer> heap = new TopKHeap<>(3, Comparing.<Integer>defaultComparator());
    for (int i = 0; i < 20; i++) {
      System.out.println(heap.peek());

      if(i > 0) {
        assertEquals(Math.max(0, i - 3), heap.peek().intValue());
      }
      heap.offer(i);
    }

    assertEquals(Arrays.asList(19, 18, 17), heap.getSorted());
  }
}