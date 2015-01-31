package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.fn.TransformFn;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapFnsTest {
	@Test
	public void firstsTest() {
		Map<Integer, Integer> singletonMap = Collections.singletonMap(7,3);
		assertEquals(7, MapFns.firstKey(singletonMap).intValue());
		assertEquals(3, MapFns.firstValue(singletonMap).intValue());
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

		assertEquals(MapFns.mapKeys(offByOne, new TransformFn<Integer, Integer>() {
			@Override
			public Integer transform(Integer input) {
				return input+1;
			}
		}), backwards);
	}
}