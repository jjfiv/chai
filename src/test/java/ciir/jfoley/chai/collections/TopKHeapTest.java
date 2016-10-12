package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.util.Comparing;
import ciir.jfoley.chai.collections.util.ListFns;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TopKHeapTest {

  @Test
  public void testGetSorted() throws Exception {
    TopKHeap<Integer> heap = new TopKHeap<>(3, Comparing.<Integer>defaultComparator());
    for (int i = 0; i < 20; i++) {
      if(i > 0) {
        Integer actual = heap.peek();
        assertNotNull(actual);
        assertEquals(Math.max(0, i - 3), actual.intValue());
      }
      heap.offer(i);
    }

    assertEquals(Arrays.asList(19, 18, 17), heap.getSorted());
  }

  @Test
  public void testAddInBulk() {
    TopKHeap<Integer> heap = new TopKHeap<>(3, Comparing.<Integer>defaultComparator());

    heap.addAll(IntRange.exclusive(0, 20).chai().reverse().intoList());
    assertEquals(Arrays.asList(19, 18, 17), heap.getSorted());
  }


  @Test
  public void testTopCharacters() throws Exception {
    TObjectIntHashMap<Character> items = new TObjectIntHashMap<>();
    int z = 'z';
    int a = 'a';
    for (int i = a; i <= z; i++) {
      items.put((char) i, i);
    }

    List<TopKHeap.Weighted<Character>> weighteds = TopKHeap.takeTop(10, items);

    List<Character> truth = new ArrayList<>();
    for (char c : "zyxwvutsrq".toCharArray()) { truth.add(c); }
    assertEquals(truth, ListFns.map(weighteds, x -> x.object));

    // by construction:
    for (TopKHeap.Weighted<Character> weighted : weighteds) {
      int c = (int) weighted.weight;
      assertEquals(c, weighted.object.charValue());
    }

  }
}