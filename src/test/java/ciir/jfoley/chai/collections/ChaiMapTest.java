package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.chained.ChaiMap;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ChaiMapTest {

	@Test
	public void testFromToPairs() {
		ChaiMap<Integer, Integer> map = ChaiMap.create(
			Pair.of(1, 10),
			Pair.of(2, 20),
			Pair.of(3, 30)
		);

		assertEquals(40, map.getOrElse(4, 40).intValue());
		assertEquals(30, map.getOrElse(3, 30).intValue());

		assertEquals(3, map.invert().get(30).intValue());
		assertEquals(3, map.invert(new ArrayListMap<Integer, Integer>()).get(30).intValue());
	}

	@Test
	public void testReadOnly() {
		ChaiMap<Integer, Integer> map = ChaiMap.create(
			Pair.of(1, 10),
			Pair.of(2, 20),
			Pair.of(3, 30)
		).readOnly();

		try {
			map.put(3, 30);
			fail("put on readonly should throw an error.");
		} catch (UnsupportedOperationException ex) {
			assertNotNull(ex);
		}
	}

	@Test
	public void testVals() {
		assertEquals(
			Arrays.asList(10, 20, 30),
			ChaiMap.create(
				Pair.of(1, 10),
				Pair.of(2, 20),
				Pair.of(3, 30)
			).readOnly().vals().sorted().intoList());
	}

	@Test
	public void testPairsCanCopy() {
		ChaiMap<Integer, Integer> map = ChaiMap.create(
			Pair.of(1, 10),
			Pair.of(2, 20),
			Pair.of(3, 30)
		).readOnly();
		assertEquals(map, map.pairs().<Integer,Integer>intoMap());
	}

}