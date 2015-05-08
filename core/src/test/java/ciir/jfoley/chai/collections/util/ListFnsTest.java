package ciir.jfoley.chai.collections.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ListFnsTest {
  @Test
  public void testTake() {
    List<Integer> input = Arrays.asList(1,2,3,4,5,6);
    assertEquals(Arrays.asList(1,2), ListFns.take(input, 2));
    assertEquals(Arrays.asList(1,2,3,4), ListFns.take(input, 4));
    assertEquals(Collections.<Integer>emptyList(), ListFns.take(input, 0));
    assertEquals(input, ListFns.take(input, 10));
  }

}