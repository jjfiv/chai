package ciir.jfoley.chai.collections.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ArrayFnsTest {

  @Test
  public void testConcat() throws Exception {
    byte[] lhs = {1,2,3};
    byte[] rhs = {4,5,6};
    assertArrayEquals(new byte[] {1,2,3,4,5,6}, ArrayFns.concat(lhs, rhs));
  }

  @Test
  public void testCompare() {
    byte[] a = {1,2,3};
    byte[] b = {1,2,3,4};
    byte[] c = {1,2,4};

    List<byte[]> arr = Arrays.asList(c, a, b);
    Collections.sort(arr, Comparing.byteArrays());

    assertEquals(Arrays.asList(a,b,c), arr);
  }
}