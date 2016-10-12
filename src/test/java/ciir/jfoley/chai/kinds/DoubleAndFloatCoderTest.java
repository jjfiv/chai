package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.kinds.DoubleCoder;
import ciir.jfoley.chai.coders.kinds.FloatCoder;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class DoubleAndFloatCoderTest {
  @Test
  public void testDoubleCoder() {
    DoubleCoder coder = new DoubleCoder();
    Random rand = new Random();
    for (int i = 0; i < 1000; i++) {
      double val = rand.nextDouble();
      assertEquals(val, coder.read(coder.write(val)), 1e-20);
    }
  }

  @Test
  public void testFloatCoder() {
    FloatCoder coder = new FloatCoder();
    Random rand = new Random();
    for (int i = 0; i < 1000; i++) {
      float val = rand.nextFloat();
      assertEquals(val, coder.read(coder.write(val)), 1e-10);
    }
  }
}