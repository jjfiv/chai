package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.chained.ChaiIterable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetFnsTest {

	@Test
	public void testIntersectionMany() throws Exception {
		List<Set<Integer>> data = Arrays.asList(ChaiIterable.create(1,2,3).intoSet(), ChaiIterable.create(3,4,5).intoSet(), ChaiIterable.create(3,30,100).intoSet());
		Set<Integer> isect = SetFns.intersection(data);
		assertEquals(ChaiIterable.create(3).intoSet(), isect);
	}

	@Test
	public void testIntersection() throws Exception {
		Set<Integer> isect = SetFns.intersection(Arrays.asList(1, 2, 3), Arrays.asList(3, 4, 5));

		assertEquals(1, isect.size());
		assertEquals(3, (int) IterableFns.first(isect));
	}

	@Test
	public void testUnion() throws Exception {
		Set<Integer> result = SetFns.union(Arrays.asList(1, 2, 3), Arrays.asList(3, 4, 5));

		assertEquals(5, result.size());
		assertTrue(result.contains(1));
		assertTrue(result.contains(2));
		assertTrue(result.contains(3));
		assertTrue(result.contains(4));
		assertTrue(result.contains(5));
	}
}