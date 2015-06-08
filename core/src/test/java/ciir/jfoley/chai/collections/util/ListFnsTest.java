package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.IntRange;
import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.*;

import static ciir.jfoley.chai.collections.util.ListFns.*;
import static org.junit.Assert.assertEquals;

public class ListFnsTest {
  @Test
  public void testTake() {
    List<Integer> input = Arrays.asList(1,2,3,4,5,6);
    assertEquals(Arrays.asList(1,2), take(input, 2));
    assertEquals(Arrays.asList(1,2,3,4), take(input, 4));
    assertEquals(Collections.<Integer>emptyList(), take(input, 0));
    assertEquals(input, take(input, 10));
  }

  @Test
  public void testPairs() {
    List<Integer> input = Arrays.asList(1,2,3,4);
    assertEquals(
        new HashSet<>(pairs(input)),
        new HashSet<>(Arrays.asList(
            Pair.of(1,2),
            Pair.of(1,3),
            Pair.of(1,4),
            Pair.of(2,3),
            Pair.of(2,4),
            Pair.of(3,4)
        )));
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
        partition(IntRange.exclusive(0, 100), 5));

    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.exclusive(0,20),
            IntRange.exclusive(20,40),
            IntRange.exclusive(40,60),
            IntRange.exclusive(60,80),
            IntRange.exclusive(80,99)
        ),
        partition(IntRange.exclusive(0, 99), 5));

    assertEquals(
        Arrays.<List<Integer>>asList(
            IntRange.exclusive(0,16),
            IntRange.exclusive(16,32),
            IntRange.exclusive(32,48),
            IntRange.exclusive(48,64),
            IntRange.exclusive(64,81)
        ),
        partition(IntRange.exclusive(0, 81), 5));
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
        partitionRoundRobin(IntRange.exclusive(0, 6), 5));

    List<List<Integer>> data = partitionRoundRobin(IntRange.exclusive(0,2400), 50);
    assertEquals(50, data.size());
  }

  @Test
  public void testUnique() {
    assertEquals(Arrays.asList(1, 2, 3, 4), unique(Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4)));
  }

  @Test
  public void testZip() {
    List<Pair<Integer,Integer>> zipped = ListFns.zip(IntRange.inclusive(0, 100), IntRange.inclusive(0,3));
    assertEquals(
        Arrays.asList(
            Pair.of(0,0),
            Pair.of(1,1),
            Pair.of(2,2),
            Pair.of(3,3)
        ),
        zipped);
  }

  @Test
  public void testPushToCopy() {
    List<Integer> orig = new ArrayList<>(Arrays.asList(1,2,3));
    List<Integer> ordered = pushToCopy(orig, 4);
    assertEquals((List<Integer>) IntRange.inclusive(1,4), ordered);

    // mutate original:
    orig.set(0, 3);
    // now orig is a palindrome:
    assertEquals(Arrays.asList(3,2,3), orig);
    // but ordered is unaffected:
    assertEquals((List<Integer>) IntRange.inclusive(1,4), ordered);
  }

  @Test
  public void testSlice() {
    IntRange x = IntRange.exclusive(0, 100);
    assertEquals((List<Integer>) IntRange.exclusive(0,10), slice(x, -33, 10));
    assertEquals((List<Integer>) IntRange.exclusive(90,100), slice(x, 90, 400));
  }

  @Test
  public void repeat() {
    assertEquals(Arrays.asList(1,2,3), ListFns.repeatUntilAtLeast(Arrays.asList(1,2,3), 3));
    assertEquals(
        Arrays.asList(1,2,3,1,2,3,1,2,3,1,2,3),
        ListFns.repeatUntilAtLeast(Arrays.asList(1,2,3), 10));
  }
}