package edu.umass.cs.ciir.jfoley.chai.collections;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FnsTest {

	@Test
	public void testSorted() {
		List<Integer> data = Arrays.asList(4,2,1,3,5);
		assertEquals(Arrays.asList(1, 2, 3, 4, 5),
			Fns.sorted(data));
		assertEquals(Arrays.asList(5,4,3,2,1),
			Fns.sorted(data, Collections.<Integer>reverseOrder()));
	}


}