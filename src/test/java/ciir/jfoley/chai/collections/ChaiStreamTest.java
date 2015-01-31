package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.fn.Fns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.stream.ChaiStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ChaiStreamTest {
	@Test
	public void testMap() {
		List<String> data = ChaiStream.create(1, 2, 3).map(new TransformFn<Integer, Integer>() {
			@Override
			public Integer transform(Integer input) {
				return input * 2;
			}
		}).map(new TransformFn<Integer, String>() {

			@Override
			public String transform(Integer input) {
				return Integer.toString(input);
			}
		}).intoList();

		assertEquals(Arrays.asList("2","4", "6"), data);
	}

	@Test
	public void testSorting() {
		assertEquals(Arrays.asList(1,2,3,4,5),
			ChaiStream.create(2,3,4,1,5).sorted().intoList());

		assertEquals(Arrays.asList(5, 4, 3, 2, 1),
			ChaiStream.create(2, 3, 4, 1, 5).sorted(Collections.<Integer>reverseOrder()).intoList());
	}

	@Test
	public void testMapAndFilter() {
		List<Integer> data = ChaiStream.create(1,2,3,4,5,6,7,8,9,10).map(new TransformFn<Integer, Integer>() {

			@Override
			public Integer transform(Integer input) {
				return input / 2;
			}
		}).filter(new PredicateFn<Integer>() {
			@Override
			public boolean test(Integer input) {
				return input <= 3;
			}
		}).intoList();

		assertEquals(Arrays.asList(0,1,1,2,2,3,3), data);
	}

	@Test
	public void testGroupById() {
		Map<Integer, List<Integer>> items = ChaiStream.create(1,1,2,2,3,4,5).groupBy(Fns.<Integer>identity());

		assertEquals(5, items.size());
		assertEquals(Arrays.asList(1,1), items.get(1));
		assertEquals(Arrays.asList(2,2), items.get(2));
		assertEquals(Arrays.asList(3), items.get(3));
		assertEquals(Arrays.asList(4), items.get(4));
		assertEquals(Arrays.asList(5), items.get(5));
	}

}