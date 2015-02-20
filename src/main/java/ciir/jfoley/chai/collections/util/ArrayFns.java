package ciir.jfoley.chai.collections.util;

import java.util.Arrays;

/**
 * @author jfoley
 */
public class ArrayFns {

  /** Concatenate byte arrays somewhat efficiently, making a new array */
  public static byte[] concat(byte[] lhs, byte[] rhs) {
    byte[] output = Arrays.copyOf(lhs, lhs.length + rhs.length);
    System.arraycopy(rhs, 0, output, lhs.length, rhs.length);
    return output;
  }
}
