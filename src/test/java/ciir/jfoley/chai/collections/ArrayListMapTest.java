package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.chained.ChaiMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayListMapTest {
	@Test
	public void testPutOver() {
		Map<Integer, Integer> map = new ArrayListMap<>();

		map.put(1, 3);
		map.put(2, 3);
		map.put(3, 3);
		map.put(3, 4);
		map.put(3, 5);
		map.put(3, 6);

		assertEquals(map.size(), 3);
		assertEquals(
			ChaiMap.create(Pair.of(1,3), Pair.of(2,3), Pair.of(3,6)),
			map);
	}

	@Test
	public void testRemoveAll() {
		Map<Integer, Integer> map = new ArrayListMap<>();

		map.put(1, 3);
		map.put(2, 3);
		map.put(3, 3);
		map.put(3, 4);
		map.put(3, 5);
		map.put(3, 6);

		assertEquals(map.size(), 3);
		assertEquals(
			ChaiMap.create(Pair.of(1,3), Pair.of(2,3), Pair.of(3,6)),
			map);

		map.remove(1);
		assertEquals(
			ChaiMap.create(Pair.of(2,3), Pair.of(3,6)),
			map);

		map.remove(3);
		assertEquals(
			ChaiMap.create(Pair.of(2, 3)),
			map);

		map.remove(2);
		assertTrue(map.isEmpty());
	}
}