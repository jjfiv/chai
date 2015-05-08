package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.chained.ChaiIterable;
import ciir.jfoley.chai.fn.Fns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.TransformFn;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ChaiIterableTest {
	@Test
	public void testMap() {
		List<String> data = ChaiIterable.create(1, 2, 3).map(new TransformFn<Integer, Integer>() {
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
			ChaiIterable.create(2, 3, 4, 1, 5).sorted().intoList());

		assertEquals(Arrays.asList(5, 4, 3, 2, 1),
			ChaiIterable.create(2, 3, 4, 1, 5).sorted(Collections.<Integer>reverseOrder()).intoList());
	}

	@Test
	public void testMapAndFilter() {
		List<Integer> data = ChaiIterable.create(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(new TransformFn<Integer, Integer>() {

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
		Map<Integer, List<Integer>> items = ChaiIterable.create(1, 1, 2, 2, 3, 4, 5).groupBy(Fns.<Integer>identity());

		assertEquals(5, items.size());
		assertEquals(Arrays.asList(1,1), items.get(1));
		assertEquals(Arrays.asList(2,2), items.get(2));
		assertEquals(Arrays.asList(3), items.get(3));
		assertEquals(Arrays.asList(4), items.get(4));
		assertEquals(Arrays.asList(5), items.get(5));
	}

  @Test
  public void testHeapStuff() {
    assertEquals(Arrays.asList(10,9,8,7), ChaiIterable.create(5,6,7,8,9,10).maxK(4).intoList());
    assertEquals(Arrays.asList(5, 6, 7, 8), ChaiIterable.create(5, 6, 7, 8, 9, 10).minK(4).intoList());
  }

  @Test
  public void testBatches() {
    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.inclusive(0, 9),
            IntRange.inclusive(10, 19)
        ),
        ChaiIterable.create(IntRange.exclusive(0, 20)).batches(10).intoList());
  }

	@Test
	public void testReverse() {
		assertEquals(
			Arrays.asList(9,8,7,6,5,4,3,2,1,0),
			IntRange.exclusive(0, 10).chai().reverse().intoList());
	}
}