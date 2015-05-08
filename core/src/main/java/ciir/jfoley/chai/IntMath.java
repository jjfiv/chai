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
}
