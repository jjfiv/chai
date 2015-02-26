package ciir.jfoley.chai.collections.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

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

  public static List<Integer> toList(final int[] data) {
    return new AbstractList<Integer>() {
      @Override public Integer get(int i) { return data[i]; }
      @Override public int size() { return data.length; }
    };
  }

  public static List<Double> toList(final double[] x) {
    return new AbstractList<Double>() {
      @Override public int size() { return x.length; }
      @Override public Double get(int index) { return x[index]; }
    };
  }
}
