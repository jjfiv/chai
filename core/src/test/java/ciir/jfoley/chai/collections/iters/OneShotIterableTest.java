package ciir.jfoley.chai.collections.iters;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OneShotIterableTest {
	@Test
	public void makesMagicIterable() {
		Iterable<Integer> foo = new OneShotIterable<>(Arrays.asList(1,2,3,4).iterator());

		List<Integer> xs = new ArrayList<>();
		for (int x : foo) {
			assertTrue(x > 0);
			xs.add(x);
		}

		assertEquals(Arrays.asList(1,2,3,4), xs);
	}

}