package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.IntRange;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListFnsTest {
  @Test
  public void testTake() {
    List<Integer> input = Arrays.asList(1,2,3,4,5,6);
    assertEquals(Arrays.asList(1,2), ListFns.take(input, 2));
    assertEquals(Arrays.asList(1,2,3,4), ListFns.take(input, 4));
    assertEquals(Collections.<Integer>emptyList(), ListFns.take(input, 0));
    assertEquals(input, ListFns.take(input, 10));
  }

  @Test
  public void testPartition() {
    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.exclusive(0,20),
            IntRange.exclusive(20,40),
            IntRange.exclusive(40,60),
            IntRange.exclusive(60,80),
            IntRange.exclusive(80,100)
        ),
        ListFns.partition(IntRange.exclusive(0, 100), 5));

    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.exclusive(0,20),
            IntRange.exclusive(20,40),
            IntRange.exclusive(40,60),
            IntRange.exclusive(60,80),
            IntRange.exclusive(80,99)
        ),
        ListFns.partition(IntRange.exclusive(0, 99), 5));

    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.exclusive(0,16),
            IntRange.exclusive(16,32),
            IntRange.exclusive(32,48),
            IntRange.exclusive(48,64),
            IntRange.exclusive(64,81)
        ),
        ListFns.partition(IntRange.exclusive(0, 81), 5));
  }

  @Test
  public void testPartitionRoundRobin() {
    assertEquals(
        Arrays.asList(
            Arrays.asList(0, 5),
            Collections.singletonList(1),
            Collections.singletonList(2),
            Collections.singletonList(3),
            Collections.singletonList(4)
        ),
        ListFns.partitionRoundRobin(IntRange.exclusive(0, 6), 5));
  }
}