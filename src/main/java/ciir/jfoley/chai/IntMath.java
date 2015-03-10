package ciir.jfoley.chai;

/**
 * @author jfoley.
 */
public class IntMath {
	public static int nearestPowerOfTwo(int x) {
		if(x < 0) throw new IllegalArgumentException();
		if(x == 0) return 1;

		int nz = Integer.numberOfLeadingZeros(x);
		int numberOfOnes = 32 - nz;
		if(numberOfOnes > 31) throw new RuntimeException();
		return 1 << numberOfOnes;
	}
}
