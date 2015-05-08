package ciir.jfoley.chai.collections.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayFnsTest {

  @Test
  public void testConcat() throws Exception {
    byte[] lhs = {1,2,3};
    byte[] rhs = {4,5,6};
    assertArrayEquals(new byte[] {1,2,3,4,5,6}, ArrayFns.concat(lhs, rhs));
  }
}