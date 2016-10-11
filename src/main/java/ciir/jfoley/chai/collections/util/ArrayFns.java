package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.lang.Module;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jfoley
 */
public class ArrayFns extends Module {

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

  public static double[] fromList(List<Double> xs) {
    double[] data = new double[xs.size()];
    for (int i = 0; i < xs.size(); i++) {
      data[i] = xs.get(i);
    }
    return data;
  }

  /**
   * Reverse an array
   * @param input an array of doubles
   * @return a copy of the input array, reversed
   */
  public static double[] reverse(double[] input) {
    double[] output = new double[input.length];
    for(int i=0; i<input.length; i++) {
      output[input.length-i-1] = input[i];
    }
    return output;
  }

  public static <T> List<T> toList(T[] data, int startIndex) {
    return new ArrayList<>(Arrays.asList(data).subList(startIndex, data.length));
  }

  /**
   * Compare two byte arrays. Based on original implementation in Galago.
   * @param one the left-hand byte[]
   * @param two the right-hand byte[]
   * @return an integer that satisfies Java's comparing interfaces.
   */
  public static int compare(byte[] one, byte[] two) {
    int sharedLength = Math.min(one.length, two.length);

    for (int i = 0; i < sharedLength; i++) {
      int a = ((int) one[i]) & 0xFF;
      int b = ((int) two[i]) & 0xFF;
      int result = a - b;

      if (result < 0) {
        return -1;
      }
      if (result > 0) {
        return 1;
      }
    }

    return one.length - two.length;
  }

}
