package ciir.jfoley.chai.collections;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IterableFnsTest {

	@Test
	public void testSorted() {
		List<Integer> data = Arrays.asList(4,2,1,3,5);
		assertEquals(Arrays.asList(1, 2, 3, 4, 5),
			IterableFns.sorted(data));
		assertEquals(Arrays.asList(5,4,3,2,1),
			IterableFns.sorted(data, Collections.<Integer>reverseOrder()));
	}

	@Test
	public void testConcat() {
		assertEquals(
			Arrays.asList(1,2,3,4,5,6),
			IterableFns.intoList(IterableFns.lazyConcat(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6))));
		// Test list version:
		List<Integer> lazilyMerged = ListFns.lazyConcat(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
		assertEquals(Arrays.asList(1,2,3,4,5,6), lazilyMerged);
	}

}