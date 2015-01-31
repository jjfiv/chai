package ciir.jfoley.chai.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListBasedOrderedSetTest {

	@Test
	public void enforcesUnique() {
		Set<Integer> data = new ListBasedOrderedSet<>();
		data.addAll(Arrays.asList(1,2,3,4,1,2,3,4,1,2,5,4,2,3));
		assertEquals(data.size(), 5);
		assertTrue(data.contains(1));
		assertEquals(Arrays.asList(1, 2, 3, 4, 5), new ArrayList<>(new TreeSet<>(data)));
	}

}