package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.chained.ChaiMap;
import ciir.jfoley.chai.collections.util.MapFns;
import ciir.jfoley.chai.fn.TransformFn;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapFnsTest {
  @Test
  public void firstsTest() {
    Map<Integer, Integer> singletonMap = Collections.singletonMap(7,3);
    Integer key = MapFns.firstKey(singletonMap);
    Integer val = MapFns.firstValue(singletonMap);
    assertNotNull(key);
    assertNotNull(val);
    assertEquals(7, key.intValue());
    assertEquals(3, val.intValue());
  }

  @Test
  public void invertMapTest() {
    Map<String, Integer> original = new HashMap<>();
    original.put("one", 1);
    original.put("two", 2);
    original.put("three", 3);

    Map<Integer, String> backwards = MapFns.invert(original);
    assertEquals("one", backwards.get(1));
    assertEquals("two", backwards.get(2));
    assertEquals("three", backwards.get(3));

    Map<Integer, String> offByOne = MapFns.ofListIndex(Arrays.asList("one", "two", "three"));
    assertEquals("one", offByOne.get(0));
    assertEquals("two", offByOne.get(1));
    assertEquals("three", offByOne.get(2));

    assertEquals(MapFns.mapKeys(offByOne, input -> input+1), backwards);
  }

  @Test
  public void countUp() {
    ChaiMap<Integer, Integer> count = ChaiMap.create();
    for (int i = 0; i < 100; i++) {
      MapFns.addOrIncrement(count, i%10, 1);
    }

    assertEquals(10, count.get(0).intValue());
    assertEquals(10, count.get(1).intValue());
    Set<Integer> vals = count.vals().intoSet();
    assertEquals(1, vals.size());
    assertEquals(10, vals.iterator().next().intValue());
  }

  @Test
  public void testMissing() {
    ChaiMap<Integer, Integer> map = ChaiMap.create( Pair.of(3, 4), Pair.of(10, 10) );
    assertEquals(4, map.getOrElse(3, -1).intValue());
    assertEquals(-1, map.getOrElse(5, -1).intValue());
    assertEquals(-1, MapFns.getOrElse(map, 5, -1).intValue());
  }
}