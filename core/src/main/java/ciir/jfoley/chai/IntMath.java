package ciir.jfoley.chai;

import ciir.jfoley.chai.lang.Module;

/**
 * @author jfoley.
 */
public class IntMath extends Module {
	public static int nearestPowerOfTwo(int x) {
		if(x < 0) throw new IllegalArgumentException();
		if(x == 0) return 1;

		int nz = Integer.numberOfLeadingZeros(x);
		int numberOfOnes = 32 - nz;
		if(numberOfOnes > 31) throw new RuntimeException();
		return 1 << numberOfOnes;
	}

	/**
	 * For when you have a long and you want to die if it doesn't fit in an int.
	 * @param l the long that hopefully is less than 2^31-1
	 * @return (int) l if possible.
	 */
	public static int fromLong(long l) {
		if(l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) throw new NumberFormatException("Can't fit the long into an int!");
		return (int) l;
	}
}
