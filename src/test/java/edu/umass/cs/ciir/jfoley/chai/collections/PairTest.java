package edu.umass.cs.ciir.jfoley.chai.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairTest {

	@Test
	public void kindOfPointless() {
		Pair<String,Integer> count = Pair.of("Hello", 10);

		assertEquals(10, count.right.intValue());
		assertEquals("Hello", count.left);
	}
}