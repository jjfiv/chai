package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.collections.IntRange;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class BitVectorTest {
  @SuppressWarnings("RedundantCast")
  @Test
  public void testBV() {
    BitVector vec = new BitVector(100);
    vec.set(13);
    vec.set(23);
    vec.set(99);

    assertEquals(Arrays.asList(13, 23, 99), (List<Integer>) vec.extract());
  }

  @Test
  public void testRandomly() {
    BitVector vec = new BitVector(2000);
    Set<Integer> data = new HashSet<>();

    Random rand = new Random();
    for (int i = 0; i < 100; i++) {
      int value = rand.nextInt(2000);
      data.add(value);
      vec.set(value);
    }

    IntList sd = new IntList(data);
    sd.sort();

    assertEquals(sd, vec.extract());
  }

  @SuppressWarnings("RedundantCast")
  @Test
  public void testShiftLeft() {
    BitVector vec = new BitVector(100);
    vec.set(13);
    vec.set(23);
    vec.set(64);
    vec.set(65);
    vec.set(99);

    vec.shiftLeft(16);

    assertEquals(
        Arrays.asList(23, 64, 65, 99).stream().map(x -> x - 16).collect(Collectors.toList()), (List<Integer>) vec.extract());
  }

  @Test
  public void massSetBits() {
    BitVector vec = new BitVector(200); // 4 longs
    vec.set(3, 194);
    assertEquals((List<Integer>) IntRange.exclusive(3, 194), (List<Integer>) vec.extract());

    vec.clear(3, 64);
    assertEquals((List<Integer>) IntRange.exclusive(64, 194), (List<Integer>) vec.extract());

    vec.clear(3, 194);
    assertEquals(0, vec.count());
  }

  @Test
  public void massiveOR() {
    BitVector vec = new BitVector(200); // 4 longs
    vec.set(3, 194);

    BitVector rhs = new BitVector(200);
    rhs.set(194, 230);
    assertEquals((List<Integer>) IntRange.exclusive(3, 194), (List<Integer>) vec.extract());

    vec.or(rhs);
    assertEquals((List<Integer>) IntRange.exclusive(3, 230), (List<Integer>) vec.extract());
  }

  @Test
  public void massiveAND() {
    BitVector vec = new BitVector(200); // 4 longs
    vec.set(3, 194);

    BitVector rhs = new BitVector(200);
    rhs.set(194, 230);
    assertEquals((List<Integer>) IntRange.exclusive(3, 194), (List<Integer>) vec.extract());

    vec.or(rhs);
    assertEquals((List<Integer>) IntRange.exclusive(3, 230), (List<Integer>) vec.extract());

    BitVector few = new BitVector(231);
    few.set(73);
    few.set(37);
    few.set(231);

    vec.and(few);
    assertEquals(Arrays.asList(37, 73), (List<Integer>) vec.extract());
  }

  @Test
  public void massiveComplement() {
    BitVector vec = new BitVector(200); // 4 longs
    vec.set(3);
    vec.complement();


    BitVector other = new BitVector(10);
    other.set(0, 6);
    vec.and(other);

    assertEquals(Arrays.asList(0,1,2,4,5), vec.extract());

  }
}