package ciir.jfoley.chai.collections.iters;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class UntilNullIteratorTest {
	class Returns123 implements UntilNullGenerator<Integer> {
		public int current;

		public Returns123() {
			this.current = 0;
		}

		@Override
		public Integer next() {
			++current;
			if(current > 3) {
				return null;
			}
			return current;
		}
	}

	@Test
	public void testUntilNullAsIterator() {
		Iterable<Integer> oneTwoThree = new UntilNullIterator<>(new Returns123()).iterable();

		Iterator<Integer> iter = oneTwoThree.iterator();
		assertTrue(iter.hasNext());
		assertEquals(1, iter.next().intValue());
		assertTrue(iter.hasNext());
		assertEquals(2, iter.next().intValue());
		assertTrue(iter.hasNext());
		assertEquals(3, iter.next().intValue());
		assertFalse(iter.hasNext());
	}
}