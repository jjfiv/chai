package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.kinds.FixedSize;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class FixedSizeTest {
  @Test
  public void testIntsRandomly() {
    Coder<Integer> c = FixedSize.ints;
    Random r = new Random();
    for (int i = 0; i < 1000; i++) {
      int x = r.nextInt();
      assertEquals(x, c.read(c.write(x)).intValue());
    }
  }

  @Test
  public void testLongsRandomly() {
    Coder<Long> c = FixedSize.longs;
    Random r = new Random();
    for (int i = 0; i < 1000; i++) {
      long x = r.nextLong();
      assertEquals(x, c.read(c.write(x)).longValue());
    }
  }
}