package ciir.jfoley.chai.random;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class SampleTest {

  @Test
  public void testByRandomWeight() throws Exception {
    // smoke test
    List<Integer> foo = Arrays.asList(1,1,1,1,1,1);
    assertEquals(Arrays.asList(1,1), Sample.byRandomWeight(foo, 2, new Random(13)));

    for (int i = 0; i < 100; i++) {
      assertEquals(1, Sample.once(foo).intValue());
    }
  }

  @Test
  public void testGetsFromAll() {
    List<Integer> foo = Arrays.asList(1,2);

    boolean foundOne = false;
    boolean foundTwo = false;
    for (int i = 0; i < 100; i++) {
      int x = Sample.once(foo);
      assertTrue(x <= 2 && x >= 1);
      if(x == 1) foundOne = true;
      if(x == 2) foundTwo = true;
    }
    assertTrue(foundOne);
    assertTrue(foundTwo);
  }

  @Test
  public void testByRandomWeightMockRand() throws Exception {
    // smoke test
    List<Integer> foo = Arrays.asList(1,2,3,4,5,6);
    assertEquals(Arrays.asList(1, 2), Sample.byRandomWeight(foo, 2, new Random() {
      @Override
      public int nextInt() {
        return 4; // by fair dice roll... jk.
        // A flat value means that the sampler becomes a first() kind of operation.
      }
    }));
  }

}