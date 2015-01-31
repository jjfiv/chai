package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.chained.ChaiMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

	}

}