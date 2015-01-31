package edu.umass.cs.ciir.jfoley.chai.collections;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListBasedOrderedSetTest {

	@Test
	public void enforcesUnique() {
		Set<Integer> data = new ListBasedOrderedSet<>();
		data.addAll(Arrays.asList(1,2,3,4,1,2,3,4,1,2,5,4,2,3));
		assertEquals(data.size(), 5);
		assertTrue(data.contains(1));
		assertEquals(Arrays.<Object>asList(1, 2, 3, 4, 5), data.stream().sorted().collect(Collectors.toList()));
	}

}