package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class IntListTest {

	@Test
	public void testAdd() throws Exception {
		IntList test = new IntList(2);
		test.add(1);
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(5);

		assertEquals(Arrays.asList(1,2,3,4,5), test);
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, test.asArray());
		assertEquals(8, test.size);
	}

	@Test
	public void testAddAll() {
		List<Integer> original = Arrays.asList(1,2,3,4,5);
		IntList test = new IntList(original);
		assertEquals(original, test);
		assertEquals(8, test.size);
	}

}