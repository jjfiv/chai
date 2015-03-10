package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.util.Arrays;

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
		assertEquals(8, test.size);
	}
}