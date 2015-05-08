package ciir.jfoley.chai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntMathTest {

	@Test
	public void testNearestPowerOfTwo() throws Exception {
		assertEquals(1, IntMath.nearestPowerOfTwo(0));
		assertEquals(2, IntMath.nearestPowerOfTwo(1));
		assertEquals(4, IntMath.nearestPowerOfTwo(2));
		assertEquals(8, IntMath.nearestPowerOfTwo(7));
		assertEquals(16, IntMath.nearestPowerOfTwo(8));
		assertEquals(16, IntMath.nearestPowerOfTwo(9));
		assertEquals(32, IntMath.nearestPowerOfTwo(16));
	}
}